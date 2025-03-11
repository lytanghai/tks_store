let variantAttributes = [];

function deleteProduct(id) {
    fetch('/api/product/delete', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ id: id })
    })
    .then(data => {
        sessionStorage.setItem('popupMessage', 'success');
        sessionStorage.setItem('popupAction', 'delete');
        location.reload();
    })
     .catch(error => {
        showPopUpMessage('error', 'delete');
    });
}

function updateProduct() {
    let formData = {
        id: document.getElementById("product_id_edit").value,
        name_en: document.getElementById("product_name_en_edit").value,
        name_kh: document.getElementById("product_name_kh_edit").value,
        code: document.getElementById("product_code_edit").value,
        category: {
            id: document.getElementById("product_select").value
        },
        currency: document.getElementById("product_currency_edit").value,
        sale_price: document.getElementById("product_sale_price_edit").value,
        description: document.getElementById("product_description_edit").value
    };

    fetch('/api/product/update', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
    .then(response => response.json())
    .then(data => {
        sessionStorage.setItem('popupMessage', 'success');
        sessionStorage.setItem('popupAction', 'update');
        showPopUpMessage('success', 'update');
    })
    .catch(error => {
        showPopUpMessage('error', 'update');
    });
}

async function uploadProduct() {
    let fileInput = document.getElementById('product_variant_image_value'); // Assuming your input file field
    let files = fileInput.files;
    let formData = new FormData();

    let prodNameEn = document.getElementById("product_name_en_edit").value;
    let prodNameKh = document.getElementById("product_name_kh_edit").value;
    if(prodNameEn === '' && prodNameKh === '') {
        alert("ឈ្មោះទំនិញមិនអាចទទេរបានទេ!");
        return;
    }

    let categoryId = document.getElementById("product_select").value;
    if(categoryId === '') {
         alert("ប្រភេទទំនិញមិនអាចទទេរបានទេ!");
         return;
    }

    let salePrice = parseFloat(document.getElementById("product_sale_price_edit").value);
    if (isNaN(salePrice)) {  // ✅ Fix: Use isNaN (correct function)
        alert("តម្លៃទំនិញមិនអាចទទេរបានទេ!");
        return;
    }

    let salePriceCurrency = document.getElementById("product_currency_edit").value;
    if(salePriceCurrency === '') {
        alert("រូបីយប័ណ្ណមិនអាចទទេរបានទេ!");
        return;
    }

    let jsonData = {
        product: {
            name_en: prodNameEn,
            name_kh: prodNameKh,
            code: document.getElementById("product_code_edit").value,
            category: { id: categoryId },
            sale_price: salePrice,
            currency: salePriceCurrency,
            description: document.getElementById("product_description_edit").value
        },
        variant: {
            base_price: parseFloat(document.getElementById('product_base_price_edit').value).toFixed(2),
            currency: document.getElementById('product_base_price_currency_edit').value,
            stock_quantity: parseInt(document.getElementById('product_stock_quantity_edit').value),
            sku: document.getElementById('product_stock_sku').value
        },
        images: [],
        variant_attributes: []
    };

    if(files.length > 0) {
        for (let i = 0; i < files.length; i++) {
            formData.append("images", files[i]);
            jsonData.images.push({ variant_id: files[i].variant_id, image: files[i].name });
        }
    }

    if(variantAttributes.length > 0) {
        for (let y = 0; y < variantAttributes.length; y++) {
            jsonData.variant_attributes.push({
                attribute_id: variantAttributes[y].attribute_id,
                value: variantAttributes[y].value
            });
        }
    }

    formData.append("data", new Blob([JSON.stringify(jsonData)], { type: "application/json" }));
    try {
        let response = await fetch("http://localhost:8080/api/product/upload", {
            method: "POST",
            body: formData
        });

        let result = await response.json();
    } catch (error) {
        console.error("Error uploading:", error);
    }
}

function fetchCategories() {
    fetch("/internal/category/list")
        .then(response => response.json())
        .then(categories => {
            populateCategoryDropdown(categories);
        })
        .catch(error => {
            console.error("Error fetching categories:", error);
    });
}

function fetchAttributes() {
    fetch("/internal/attribute/list")
        .then(response => response.json())
        .then(attributes => {
            populateAttributeDropdown(attributes);
        })
        .catch(error => {
            console.error("Error fetching attributes:", error);
    });
}



function openImageSlider(button) {
    let variantId = button.getAttribute("data-variant-id");

    // Step 1: Get list of image UUIDs from API
    fetch(`/api/get/images?variant_id=${variantId}`)
        .then(response => response.json())
        .then(uuids => {
            if (!uuids.length) {
                alert("❌ទំនិញនេះមិនមានរូបភាពទេ!");
                return;
            }

            imageUUIDs = uuids;
            currentImageIndex = 0;
            showImage(); // Display first image
            document.getElementById("imageSlider").style.display = "flex";
        })
        .catch(error => {
            console.error("Error fetching images:", error);
            alert("Error loading images!");
        });
}

function showImage() {
    let imageElement = document.getElementById("sliderImage");
    let uuid = imageUUIDs[currentImageIndex];

    // Step 2: Fetch individual image using UUID
    fetch(`/api/image/show?uuid=${uuid}`)
        .then(response => response.blob())  // Get binary data
        .then(blob => {
            imageElement.src = URL.createObjectURL(blob); // Convert to image URL
        })
        .catch(error => console.error("Error loading image:", error));
}

function openVariantAttributeDetail(button) {
    let variantId = button.getAttribute("data-variant-id");
    // Fetch data using AJAX
    fetch(`/api/variant-attributes/list?variant_id=${variantId}`)
    .then(response => response.json())
    .then(data => {
        let attributeList = document.getElementById("attributeList");
        attributeList.innerHTML = ""; // Clear old data

        if (data.length > 0) {
            data.forEach(attr => {
                let listItemName = document.createElement("li");
                listItemName.textContent = `${attr.attribute_name}: ${attr.value}`;
                listItemName.style.listStyle = 'none'
                listItemName.style.paddingRight = '5%'

                attributeList.appendChild(listItemName);
            });

            document.getElementById("customModal").style.display = "flex";
        } else {
            alert("No attributes found!");
        }
    })
    .catch(error => console.error("Error fetching data:", error));
}


async function updateProductDetail() {
        let fileInput = document.getElementById('product_variant_image_value');
        let files = fileInput.files;
        let formData = new FormData();
        let id = document.getElementById("product_id_edit").value;
        let productNameEn = document.getElementById("product_name_en_edit").value;
        let productNameKh = document.getElementById("product_name_kh_edit").value;
        let productCode = document.getElementById("product_code_edit").value;
        let categoryId = document.getElementById("product_select").value;
        let salePriceCurrency = document.getElementById("product_currency_edit").value;
        let salePrice = document.getElementById("product_sale_price_edit").value;
        let description = document.getElementById("product_description_edit").value;
        let basePrice = parseFloat(document.getElementById('product_base_price_edit').value);
        let basePriceCurrency = document.getElementById('product_base_price_currency_edit').value;
        let stockQuantity = parseInt(document.getElementById('product_stock_quantity_edit').value);
        let sku = document.getElementById('product_stock_sku').value;
//        let variantId = parseInt(variantId);
        let jsonData = {
            product: {
                id: id,
                name_en: productNameEn ? productNameEn : undefined,
                name_kh: productNameKh ? productNameKh : undefined,
                code: productCode || undefined,
                category: { id: categoryId ? categoryId : undefined },
                sale_price: Number(salePrice) ? Number(salePrice) : undefined,
                currency: salePriceCurrency ? salePriceCurrency : undefined,
                description: document.getElementById("product_description_edit").value || undefined
            },
            variant: {
             id: parseInt(variantId),
             base_price: basePrice || undefined,
             currency: basePriceCurrency || undefined,
             stock_quantity: stockQuantity || undefined,
             sku: sku || undefined
             },
             variant_attributes: [],
             images: []
            };

        let variantAttr = JSON.parse(variantAttributes);

        if (variantAttr.length > 0) {
            for (let i = 0; i < variantAttr.length; i++) {
                jsonData.variant_attributes.push({
                    id: variantAttr[i].id,
                    attribute_id: variantAttr[i].attribute_id,
                    name: variantAttr[i].name,
                    value: variantAttr[i].value
                });
            }
        }

        if(files.length > 0) {
            for (let i = 0; i < files.length; i++) {
                formData.append("images", files[i]);
                jsonData.images.push({ variant_id: files[i].variant_id, image: files[i].name });
            }
        }
        formData.append("data", new Blob([JSON.stringify(jsonData)], { type: "application/json" }));

        try {
            let response = await fetch("http://localhost:8080/api/product/update", {
                method: "POST",
                body: formData
            });

//            let result = await response.json();
        } catch (error) {
            console.error("Error uploading:", error);
        }
}