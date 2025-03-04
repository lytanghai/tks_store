let variantAttributes = [];
let resultList = [];
let currentTab = "Product";

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

function openCreateUpdateProductModal(element) {

        let id = '';
        let nameEn = '';
        let nameKh = '';
        let code = '';
        let categoryNameEn = '';
        let categoryNameKh = '';
        let currency = '';
        let salePrice = '';
        let description = '';

    var iconElement = document.querySelector(".icon-service-type");
    var formTitle = element.getAttribute("data-form-title");

    showTab("Product");
    if(formTitle === 'Create') {
        document.getElementById("form-modal-product-title").textContent = 'បន្ទាប់';
        document.getElementById("form-product-create-title").textContent = 'បញ្ញូលពត៍មានទំនិញ';
        iconElement.src = "/icon/new-product-icon.png";
        iconElement.alt = "new-product-icon.png";
        document.getElementById("product-category-edit").style.display = "none";
        document.getElementById("product_currency_edit").value = 'USD'
        document.getElementById("product-submit-btn").value = "create";
    } else if(formTitle == 'Update') {
        id = element.getAttribute("id");
        nameEn = element.getAttribute("data-name-en");
        nameKh = element.getAttribute("data-name-kh");
        code = element.getAttribute("data-code");
        categoryNameEn = element.getAttribute("data-categoryEn");
        categoryNameKh = element.getAttribute("data-categoryKh");
        currency = element.getAttribute("data-currency");
        salePrice = element.getAttribute("data-sale-price");
        description = element.getAttribute("data-description");

        document.getElementById("form-modal-product-title").textContent = 'បន្ទាប់';
        document.getElementById("form-product-create-title").textContent = 'កែប្រែទិន្ន័យផលិតផលចាស់';
        iconElement.src = "/icon/edit-product-icon.png";
        iconElement.alt = "edit-product-icon.png";
        document.getElementById("product-category-edit").style.display = "block";
        document.getElementById("product-category-edit").value = categoryNameEn;
        document.getElementById("product-submit-btn").value = "update";
    }

    document.getElementById("product_id_edit").value = id;
    document.getElementById("product_name_en_edit").value = nameEn;
    document.getElementById("product_name_kh_edit").value = nameKh;
    document.getElementById("product_code_edit").value = code;
    document.getElementById("product_description_edit").value = description;
    document.getElementById("product_currency_edit").value = currency;
    document.getElementById("product_sale_price_edit").value = salePrice;
    document.getElementById("myProductModal").style.display = "block";
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
            base_price: parseFloat(document.getElementById('product_base_price_edit').value),
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


function showProductVerify() {
//Product
    document.getElementById("verify-product-id").innerHTML = document.getElementById("product_id_edit").value
    document.getElementById("verify-product-nameEn").innerHTML = document.getElementById("product_name_en_edit").value
    document.getElementById("verify-product-nameKh").innerHTML = document.getElementById("product_name_kh_edit").value
    document.getElementById("verify-product-code").innerHTML = document.getElementById("product_code_edit").value

    let selectElement = document.getElementById("product_select");
    let selectedValue = selectElement.value;

    document.getElementById("verify-product-category").innerHTML = getSelectOptionTextByValue(selectElement, selectedValue);
    document.getElementById("verify-product-sale-price").innerHTML = document.getElementById("product_sale_price_edit").value
    document.getElementById("verify-product-currency").innerHTML = document.getElementById("product_currency_edit").value
    document.getElementById("verify-product-description").innerHTML = document.getElementById("product_description_edit").value

//    Variant
    document.getElementById("verify-variant-base-price").innerHTML = parseFloat(document.getElementById('product_base_price_edit').value);
    document.getElementById("verify-variant-currency").innerHTML = document.getElementById('product_base_price_currency_edit').value;
    document.getElementById("verify-variant-stock-quantity").innerHTML = parseInt(document.getElementById('product_stock_quantity_edit').value);
    document.getElementById("verify-variant-sku-code").innerHTML = document.getElementById('product_stock_sku').value

    getLiElementsContentAsArray();
    document.getElementById("verify-variant-attribute-name").innerHTML = resultList.join('</br>');

//    Image
    showVerifyImageSlider();
}
function addAttribute() {
    let selectElement = document.getElementById("product_attribute_select");
    let attributeId = parseInt(selectElement.value);
    let attributeName = selectElement.options[selectElement.selectedIndex].text;
    let value = document.getElementById("product_attribute_value").value.trim();

    if (!value) {
        alert("Please enter a value for the attribute.");
        return;
    }

    let existingIndex = variantAttributes.findIndex(attr => attr.attribute_id === attributeId);
    if (existingIndex !== -1) {
        alert("This attribute is already added!");
        return;
    }

    variantAttributes.push({
        attribute_id: attributeId,
        value: value
    });
    updateAttributeList();
}

function updateAttributeList() {
    let selectElement = document.getElementById("product_attribute_select");
    let attributeId = selectElement.value; // Get selected attribute ID
    let attributeName = selectElement.options[selectElement.selectedIndex].text; // Get selected attribute name
    let value = document.getElementById("product_attribute_value").value;

    if (!value) {
        alert("Please enter a value for the attribute.");
        return;
    }

    let listItem = document.createElement("li");
    listItem.style.display = "flex";
    listItem.style.textAlign = "center";
    listItem.style.fontSize = "1.6rem";
    listItem.style.paddingLeft = "5%";
    listItem.style.backgroundColor = "#fff";
    listItem.setAttribute("data-id", attributeId);

    let attrSpan = document.createElement("span");
    attrSpan.classList.add("attribute-name");
    attrSpan.style.flex = "1";
    attrSpan.textContent = attributeName;

    let valueSpan = document.createElement("span");
    valueSpan.classList.add("attribute-value");
    valueSpan.style.flex = "1";
    valueSpan.style.paddingLeft = "8%";
    valueSpan.textContent = value;

    let deleteButton = document.createElement("button");
    deleteButton.textContent = "Delete";
    deleteButton.style.flex = "1";
    deleteButton.style.backgroundColor = "#fff";
    deleteButton.style.width = "100px";
    deleteButton.style.paddingLeft = "8%";
    deleteButton.innerHTML = '<img src="/icon/trash.png" class="icon" alt="Trash Icon">';
    deleteButton.addEventListener("click", function() {
        removeAttributeItem(listItem);
    });

    listItem.appendChild(attrSpan);
    listItem.appendChild(valueSpan);
    listItem.appendChild(deleteButton);

    document.getElementById("attribute-list").appendChild(listItem);

    document.getElementById("product_attribute_select").value = "";
    document.getElementById("product_attribute_value").value = "";
}

function removeAttributeItem(listItem) {
    listItem.parentNode.removeChild(listItem);
    let selectElement = document.getElementById("product_attribute_select");
    let attributeId = parseInt(selectElement.value);
    let existingIndex = variantAttributes.findIndex(attr => attr.attribute_id === attributeId);
    if (existingIndex !== -1) {
        variantAttributes.splice(existingIndex, 1);
    }
}

function toggleInputFields() {
    const condition = document.getElementById("filterCondition").value;
    const inFields = document.getElementById("inConditionFields");
    const dateFilter = document.getElementById("dateFilter");
    const singleField = document.getElementById("singleConditionField");

    if (condition === "in") {
        inFields.style.display = "block";
        singleField.style.display = "none";
        dateFilter.style.display = "none";
    } else if (condition === "equal" || condition === "greater" || condition === "less") {
        inFields.style.display = "none";
        singleField.style.display = "block";
        dateFilter.style.display = "none";
    } else if (condition === "9") {
        inFields.style.display = "none";
        singleField.style.display = "none";
        dateFilter.style.display = "block";
    } else {
        inFields.style.display = "none";
        singleField.style.display = "block";
        dateFilter.style.display = "none";
    }
}

function filterInputValueDateTime() {
    let startDate = document.getElementById("productStartDate").value;
    let endDate = document.getElementById("productEndDate").value;
    let rows = document.querySelectorAll("#productTable tr");
    let searchValue = document.getElementById("search_input_product").value.toLowerCase();

    rows.forEach(row => {
        let rowText = row.textContent.toLowerCase();
        let rowDate = row.cells[8].textContent.trim();
        let showRow = rowText.includes(searchValue);

        let rowDateTime2 = new Date(rowDate);
        if (startDate) {
            let startDateTime = new Date(startDate + "T00:00:00.000");
            if (rowDateTime2 < startDateTime) {
                showRow = false;
            }
        }
        if (endDate) {
            let endDateTime = new Date(endDate + "T23:59:59.999");
            if (rowDateTime2 > endDateTime) {
                showRow = false;
            }
        }
        row.style.display = showRow ? "" : "none";
    });
}
function productFilterResults() {
    let searchValue = document.getElementById("filterValue") ? document.getElementById("filterValue").value.toLowerCase() : "";
    let searchValue1 = document.getElementById("filterValue1") ? document.getElementById("filterValue1").value.toLowerCase() : "";
    let searchValue2 = document.getElementById("filterValue2") ? document.getElementById("filterValue2").value.toLowerCase() : "";
    let selectedColumn = parseInt(document.getElementById("filterColumn").value);
    let condition = document.getElementById("filterCondition").value;
    let rows = document.querySelectorAll("#productTable tr");

    rows.forEach(row => {
        let cell = row.cells[selectedColumn];
        if (!cell) return;

        let cellText = getTextFromCell(cell).trim().toLowerCase();
        let cellNumber = extractNumber(cellText);
        let searchNumber = extractNumber(searchValue);
        let showRow = false;

        if (condition === "in") {
            showRow = cellText.includes(searchValue1) && cellText.includes(searchValue2);
        } else if (condition === "equal") {
            if (searchNumber !== null) {
                showRow = cellNumber === searchNumber;
            }
        } else if (condition === "greater") {
            let cellNumber = parseFloat(cellText);
            let searchNumber = parseFloat(searchValue);
            if (!isNaN(cellNumber) && !isNaN(searchNumber)) {
                showRow = cellNumber > searchNumber;
            }
        } else if (condition === "less") {
            let cellNumber = parseFloat(cellText);
            let searchNumber = parseFloat(searchValue);
            if (!isNaN(cellNumber) && !isNaN(searchNumber)) {
                showRow = cellNumber < searchNumber;
            }
        } else if (condition === "contain") {
             showRow = cellText.includes(searchValue) ;
         }
    row.style.display = showRow ? "" : "none";
    });
}

function getTextFromCell(cell) {
    if (cell.querySelector('ul')) {
        let text = '';
        let items = cell.querySelectorAll('li');
        items.forEach(item => {
            text += item.textContent.trim() + ' ';
        });
        return text;
    } else {
        return cell.textContent || "";
    }
}
function clearAllFilters() {
    document.getElementById("filterColumn").selectedIndex = 0;
    document.getElementById("filterCondition").selectedIndex = 0;
    document.getElementById("filterValue").value = "";
    document.getElementById("filterValue1").value = "";
    document.getElementById("filterValue2").value = "";
    document.getElementById("productStartDate").value = "";
    document.getElementById("productEndDate").value = "";

    let rows = document.querySelectorAll("#productTable tr");
    rows.forEach(row => {
        row.style.display = "";
    });
}

const selectElement = document.getElementById('filterColumn');
selectElement.addEventListener('change', function() {
    if (selectElement.value === '10') {
        document.getElementById("filterCondition").style.display = 'none'
        document.getElementById("singleConditionField").style.display = 'none'
        document.getElementById("btn-submit-filter").style.display = 'none'
        document.getElementById("btn-reset-filter").style.display = 'none'
        document.getElementById("filter-start-date").style.display = 'inline-block'
        document.getElementById("filter-end-date").style.display = 'inline-block'
    } else {
        document.getElementById("filterCondition").style.display = 'block'
        document.getElementById("singleConditionField").style.display = 'block'
        document.getElementById("btn-submit-filter").style.display = 'block'
        document.getElementById("btn-reset-filter").style.display = 'block'
        document.getElementById("filter-start-date").style.display = 'none'
        document.getElementById("filter-end-date").style.display = 'none'
    }
});

function clearProductStartDate() {
    let startDateInput = document.getElementById("productStartDate");
    startDateInput.value = "";
    startDateInput.dispatchEvent(new Event("change"));
}

function clearProductEndDate() {
    let endDateInput = document.getElementById("productEndDate");
    endDateInput.value = "";
    endDateInput.dispatchEvent(new Event("change"));
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

function populateCategoryDropdown(categories) {
    const categorySelect = document.getElementById("product_select");
    categorySelect.innerHTML = "";
    const defaultOption = document.createElement("option");
    defaultOption.text = "";
    defaultOption.value = "";
    categorySelect.appendChild(defaultOption);

    categories.forEach(category => {
        const option = document.createElement("option");
        option.value = category.id;
        option.text = category.name + ' / ' + category.name_kh;
        categorySelect.appendChild(option);
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

function populateAttributeDropdown(attributes) {
    const attributeSelect = document.getElementById("product_attribute_select");
    attributeSelect.innerHTML = "";
    const defaultOption = document.createElement("option");
    defaultOption.text = "";
    defaultOption.value = "";
    attributeSelect.appendChild(defaultOption);

    attributes.forEach(attr => {
        const option = document.createElement("option");
        option.value = attr.id;
        option.text = attr.name_kh && attr.name_kh.trim() !== ""
            ? attr.name + " ( " + attr.name_kh + " )"
            : attr.name;
        attributeSelect.appendChild(option);
    });
}

function showConfirmationModal(action, id) {
    document.getElementById("product-confirmation-modal").style.display = "block";
    const modalText = document.getElementById("product-confirm-modal-text");
    if (action === 'create') {
        modalText.textContent = 'តើអ្នកប្រាកដថាចង់បញ្ជូលផលិតផលថ្មីមែនទេ?';
    } else if (action === 'update') {
        modalText.textContent = 'តើអ្នកប្រាកដថាចង់ធ្វើការកែប្រែផលិតផលនេះទេ?';
    } else if (action === 'delete') {
        modalText.textContent = 'តើអ្នកប្រាកដថាចង់ធ្វើការលុបផលិតផលនេះទេ?';
    }
    window.currentAction = action;
    window.productId = id;
}

function closeConfirmationModal() {
    document.getElementById("product-confirmation-modal").style.display = "none";
}

function confirmProductActionConfirmation() {
    if (window.currentAction === 'create') {
        uploadProduct();
        closeModal();
    } else if (window.currentAction === 'update') {
        updateProduct();
    } else if (window.currentAction === 'delete') {
        deleteProduct(window.productId);
    }
    updateServiceIcon();
    closeConfirmationModal();
}

function handleFormSubmit(event) {
    event.preventDefault();
    const titleText = document.getElementById("form-modal-product-title").textContent.toLowerCase();
    if(titleText === 'បន្ទាប់') {

    }
    else if(titleText.includes("បញ្ជូល")) {

        showConfirmationModal('create');
    } else {
        showConfirmationModal('update');
    }
}

function updateServiceIcon() {
    const titleElement = document.getElementById("form-modal-product-title");
    const iconElement = document.querySelector(".icon-service-type");
    const titleText = titleElement.textContent.trim().toLowerCase();

    if (titleText === "create") {
        iconElement.src = "/icon/new-product-icon.png";
        iconElement.alt = "new-product-icon.png";
    } else if (titleText === "update") {
        iconElement.src = "/icon/edit-product-icon.png";
        iconElement.alt = "edit-product-icon.png";
    } else {
        iconElement.src = "/icon/category-icon.png";
        iconElement.alt = "category-icon.png";
    }
}

function checkButtonAction() {
    const button = document.getElementById("product-submit-btn").value;
    if (button === "create") {
        showConfirmationModal('create');
    } else if (button === "update") {
        showConfirmationModal('update');
    } else {
        console.error("Unknown action for product-submit-btn");
    }
}

function closeModal() {
    document.getElementById("myProductModal").style.display = "none";
}

function showTab(tabName) {
    let tabcontent = document.getElementsByClassName("tabcontent");
    let tablinks = document.getElementsByClassName("tablinks");

    for (let i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }

    for (let i = 0; i < tablinks.length; i++) {
        tablinks[i].classList.remove("active");
    }
    document.getElementById(tabName).style.display = "block";
    document.getElementById("tab-" + tabName).classList.add("active");
    currentTab = tabName;

    if(currentTab === 'Verify') {
        showProductVerify();
        truncateTextIfLongerThan200();
    }
    event.preventDefault();
}

function navigateTab(current, next) {
    showTab(next);
}

function truncateTextIfLongerThan200() {
    const labelElement = document.getElementById("verify-product-description");
    const text = labelElement.textContent;

    if (text.length > 200) {
        labelElement.textContent = text.slice(0, 200) + ".....";
    }
}

function getLiElementsContentAsArray() {
    let ulElement = document.getElementById('attribute-list');
    let liElements = ulElement.querySelectorAll('li');

    liElements.forEach((liElement, index) => {
        let attributeName = liElement.querySelector('.attribute-name').textContent;
        let attributeValue = liElement.querySelector('.attribute-value').textContent;
        let content = `អង្គធាតុទី ${index + 1}: ${attributeName} - តម្លៃអង្គធាតុ ${attributeValue} <br>`;

        if (!resultList.includes(content)) {
            resultList.push(content);
        }
    });
}

let currentImageIndex = 0;
let imageUUIDs = [];

function openImageSlider(button) {
    let variantId = button.getAttribute("data-variant-id");

    // Step 1: Get list of image UUIDs from API
    fetch(`/api/get/images?variant_id=${variantId}`)
        .then(response => response.json())  // Expecting an array of UUIDs
        .then(uuids => {
            if (!uuids.length) {
                alert("❌ទំនិញនេះមិនមានរូបភាពទេ!");
                return;
            }

            imageUUIDs = uuids;
            currentImageIndex = 0;
            showImage(); // Display first image
            document.getElementById("imageSlider").style.display = "flex"; // Show slider
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

function nextProductImage() {

    if (currentImageIndex < imageUUIDs.length - 1) {
        currentImageIndex++;
    } else if(currentImageIndex == imageUUIDs.length - 1) {
        currentImageIndex--;
    }
    showImage();
}

function prevProductImage() {
    if (currentImageIndex > 0) {
        currentImageIndex--;
    } else {
        currentImageIndex++;
    }
    showImage();
}

function closeSlider() {
    document.getElementById("imageSlider").style.display = "none";
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
//                let listItemId = document.createElement("li");
//                listItemId.textContent = `លេខរៀង: ${attr.variant_attribute_id}`;
//                attributeList.appendChild(listItemId);
//                listItemId.style.listStyle = 'none'


                let listItemName = document.createElement("li");
                listItemName.textContent = `${attr.attribute_name}: ${attr.value}`;
                listItemName.style.listStyle = 'none'
                listItemName.style.paddingRight = '5%'

                attributeList.appendChild(listItemName);
            });

            // Show the modal
            document.getElementById("customModal").style.display = "flex";
        } else {
            alert("No attributes found!");
        }
    })
    .catch(error => console.error("Error fetching data:", error));
}
function closeVariantAttributeModal() {
    document.getElementById("customModal").style.display = "none";
}

