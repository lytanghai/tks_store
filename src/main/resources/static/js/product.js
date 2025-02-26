function createNewProduct() {
    var formData = {
        name_en: document.getElementById('product_name_en_edit').value,
        name_kh: document.getElementById('product_name_kh_edit').value,
        code: document.getElementById('product_code_edit').value,
        category: { id: document.getElementById('product_select').value },
        sale_price: parseFloat(document.getElementById('product_sale_price_edit').value),
        currency: document.getElementById('product_currency_edit').value,
        description: document.getElementById('product_description_edit').value
    };
        document.addEventListener("DOMContentLoaded", function() {
            fetchCategories();
        });

    fetch('/api/product/create', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
    .then(data => {
        sessionStorage.setItem('popupMessage', 'success');
        sessionStorage.setItem('popupAction', 'create');
        location.reload();
    })
    .catch(err => {
        showPopUpMessage('error', 'create');
    })
}

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


function showLog(){
    console.log(document.getElementById('product_base_price_edit').value)
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

//    alert('openUpdateProductModal ' + formTitle )
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

let productFullCreate = null;

function createCompleteProduct() {
    productFullCreate = {
        name_en: document.getElementById('product_name_en_edit').value,
        name_kh: document.getElementById('product_name_kh_edit').value,
        code: document.getElementById('product_code_edit').value,
        category: { id: document.getElementById('product_select').value },
        sale_price: parseFloat(document.getElementById('product_sale_price_edit').value),
        currency: document.getElementById('product_currency_edit').value,
        description: document.getElementById('product_description_edit').value
    }
    productFullCreate.variant = {
        base_price: document.getElementById('product_base_price_edit').value,
        currency: document.getElementById('product_base_price_currency_edit').value,
        stock_quantity: document.getElementById('product_stock_quantity_edit').value,
        sku: document.getElementById('product_stock_sku').value,
    }

//    productFullCreate.variant_attributes = [
//    {
//
//    }
//    ]

    console.log(JSON.stringify(productFullCreate))
}


function closeUpdateProductModal() {
    document.getElementById("myProductModal").style.display = "none";
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

let selectedAttributes = new Set(); // Stores selected attribute IDs

function fetchAttributes(selectId, currentSelectedValue) {
    fetch("/internal/attribute/list")
        .then(response => response.json())
        .then(attributes => {
            populateAttributeDropdown(attributes, selectId, currentSelectedValue);
        })
        .catch(error => {
            console.error("Error fetching attributes:", error);
        });
}


//function fetchAttributes() {
//    fetch("/internal/attribute/list")
//        .then(response => response.json())
//        .then(attributes => {
//            populateAttributeDropdown(attributes);
//        })
//        .catch(error => {
//            console.error("Error fetching categories:", error);
//    });
//}


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

function populateAttributeDropdown(attributes, selectId, currentSelectedValue) {
    const selectElement = document.getElementById(selectId);
    if (!selectElement) return;

    // Clear existing options
    selectElement.innerHTML = `<option value="">Select Attribute</option>`;

    // Filter attributes: Keep the currently selected value but exclude all other selected ones
    const filteredAttributes = attributes.filter(attr => !selectedAttributes.has(attr.id) || attr.id == currentSelectedValue);

    // Append new options
    filteredAttributes.forEach(attr => {
        const option = document.createElement("option");
        option.value = attr.id;
        option.textContent = attr.name;
        if (attr.id == currentSelectedValue) option.selected = true;
        selectElement.appendChild(option);
    });
}


function updateAttribute(attrIndex, field, value) {
    if (field === 'អង្គធាតុទំនិញ') { // Attribute selection changed
        const previousValue = variant.variant_attributes[attrIndex].id;

        // Update the variant object with the new selection
        variant.variant_attributes[attrIndex].id = value;

        // Update selected attributes set
        if (previousValue) selectedAttributes.delete(previousValue); // Remove old selection
        if (value) selectedAttributes.add(value); // Add new selection

        // Re-render all dropdowns to apply updated filters
        renderAttributes();
    } else {
        // Update other fields (e.g., price)
        variant.variant_attributes[attrIndex][field] = value;
    }
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
//    alert('confirmProductActionConfirmation ' + window.currentAction )
    if (window.currentAction === 'create') {
        createCompleteProduct();
//        createNewProduct();
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
    let currentTab = "Product";

    function openModal() {
        fetchCategories();
        document.getElementById("myProductModal").style.display = "block";
        showTab(currentTab);
    }

    function closeModal() {
        document.getElementById("myProductModal").style.display = "none";
    }

//    function outsideClick(event) {
//        if (event.target === document.getElementById("myProductModal")) {
//            closeModal();
//        }
//    }

    function showTab(tabName) {
        console.log('showTab')

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
        event.preventDefault();
    }

    function navigateTab(current, next) {
        console.log('navigateTab current' + current + " next: " + next);
        showTab(next);
    }
