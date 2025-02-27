let productData = {
    product: {
        name_en: "",
        name_kh: "",
        code: "",
        category: { id: null },
        sale_price: 0,
        currency: "",
        description: ""
    },
    variant: {
        base_price: 0,
        currency: "",
        stock_quantity: 0,
        sku: ""
    },
    variant_attributes: []
};
let variantAttributes = [];
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
alert(document.getElementById("product_select").value)
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

function openUpdateProductModal(element) {
    let id = element.getAttribute("id");
    let nameEn = element.getAttribute("data-name-en");
    let nameKh = element.getAttribute("data-name-kh");
    let code = element.getAttribute("data-code");
    let categoryNameEn = element.getAttribute("data-categoryEn");
    let categoryNameKh = element.getAttribute("data-categoryKh");
    let currency = element.getAttribute("data-currency");
    let salePrice = element.getAttribute("data-sale-price");
    let description = element.getAttribute("data-description");

    var iconElement = document.querySelector(".icon-service-type");
    var formTitle = element.getAttribute("data-form-title");

    if(formTitle === 'Create') {
        document.getElementById("form-modal-product-title").textContent = 'បន្ទាប់';
        document.getElementById("form-product-create-title").textContent = 'បញ្ញូលពត៍មានទំនិញ';
        iconElement.src = "/icon/new-product-icon.png";
        iconElement.alt = "new-product-icon.png";
        document.getElementById("product-submit-btn").value = "create";
    } else if(formTitle == 'Update') {
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

function fillProductObject() {
    productData.product = {
        name_en: document.getElementById('product_name_en_edit').value,
        name_kh: document.getElementById('product_name_kh_edit').value,
        code: document.getElementById('product_code_edit').value,
        category: { id: parseInt(document.getElementById('product_select').value) },
        sale_price: parseFloat(document.getElementById('product_sale_price_edit').value),
        currency: document.getElementById('product_currency_edit').value,
        description: document.getElementById('product_description_edit').value
    };
}

function fillVariantObject() {
    productData.variant = {
        base_price: parseFloat(document.getElementById('product_base_price_edit').value),
        currency: document.getElementById('product_base_price_currency_edit').value,
        stock_quantity: parseInt(document.getElementById('product_stock_quantity_edit').value),
        sku: document.getElementById('product_stock_sku').value
    };
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

function submitProduct() {

    fillProductObject();
    fillVariantObject();
    productData.variant_attributes = [...variantAttributes];

    fetch("/api/product/full/create", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(productData)
    })
    .then(response => response.json())
    .then(data => console.log("Product created successfully:", data))
    .catch(error => console.error("Error creating product:", error));
}

function productFilterResults() {
    let searchValue = document.getElementById("search_input_product").value.toLowerCase();
    let startDate = document.getElementById("productStartDate").value;
    let endDate = document.getElementById("productEndDate").value;
    let rows = document.querySelectorAll("#productTable tr");

    rows.forEach(row => {
        let rowText = row.textContent.toLowerCase();
        let rowDate = row.cells[6].textContent.trim();

       let textareaElements = row.querySelectorAll(".product_desc");
            textareaElements.forEach(textarea => {
                rowText += " " + textarea.value.toLowerCase();
        });
        let showRow = rowText.includes(searchValue);

        let rowDateTime = new Date(rowDate);

        if (startDate) {
            let startDateTime = new Date(startDate + "T00:00:00.000");
            if (rowDateTime < startDateTime) {
                showRow = false;
            }
        }
        if (endDate) {
            let endDateTime = new Date(endDate + "T23:59:59.999");
            if (rowDateTime > endDateTime) {
                showRow = false;
            }
        }
        row.style.display = showRow ? "" : "none";
    });
}

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
//    alert('action: ' + action)
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
        submitProduct();
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

//function openModal() {
//    fetchCategories();
//    document.getElementById("myProductModal").style.display = "block";
//    showTab(currentTab);
//}

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

let resultList = [];

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

