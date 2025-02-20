document.addEventListener("DOMContentLoaded", function() {
    fetchCategories();
});

function createNewProduct() {
    var nameEn = document.getElementById('name_en_edit').value;
    var nameKh = document.getElementById('name_kh_edit').value;
    var categoryId = document.getElementById('product_select').value;
    var salePrice = document.getElementById('sale_price_edit').value;
    var currency = document.getElementById('currency_edit').value;
    var description = document.getElementById('description_edit').value;

    var formData = {
        name_en: nameEn,
        name_kh: nameKh,
        category: { id: categoryId },
        sale_price: parseFloat(salePrice),
        currency: currency,
        description: description
    };

    fetch('/product/create', {
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
    var productId = id;

    fetch('/product/delete', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ id: productId })
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
        id: document.getElementById("id_edit").value,
        name_en: document.getElementById("name_en_edit").value,
        name_kh: document.getElementById("name_kh_edit").value,
        currency: document.getElementById("currency_edit").value,
        sale_price: document.getElementById("sale_price_edit").value,
        description: document.getElementById("description_edit").value
    };

    fetch('/product/update', {
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
    let currency = element.getAttribute("data-currency");
    let salePrice = element.getAttribute("data-sale-price");
    let description = element.getAttribute("data-description");

    var iconElement = document.querySelector(".icon-service-type");
    var formTitle = element.getAttribute("data-form-title");

    if(formTitle === 'Create') {
        document.getElementById("form-modal-product-title").textContent = 'បញ្ជូលផលិតថ្មី';
        document.getElementById("product-edit-btn").textContent = 'បញ្ជូល';
        iconElement.src = "/icon/new-product-icon.png";
        iconElement.alt = "new-product-icon.png";
    } else if(formTitle == 'Update') {
        document.getElementById("form-modal-product-title").textContent = 'កែប្រែទិន្ន័យផលិតផលចាស់';
        document.getElementById("product-edit-btn").textContent = 'កែប្រែ';
        iconElement.src = "/icon/edit-product-icon.png";
        iconElement.alt = "edit-product-icon.png";
    }

    document.getElementById("id_edit").value = id;
    document.getElementById("name_en_edit").value = nameEn;
    document.getElementById("name_kh_edit").value = nameKh;
    document.getElementById("description_edit").value = description;
    document.getElementById("currency_edit").value = currency;
    document.getElementById("sale_price_edit").value = salePrice;
    document.getElementById("myProductModal").style.display = "block";
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
    fetch("/rest/category/list")
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
    defaultOption.text = "ទូទៅ";
    defaultOption.value = "General";
    categorySelect.appendChild(defaultOption);

    categories.forEach(category => {
        const option = document.createElement("option");
        option.value = category.id;
        option.text = category.name + ' / ' + category.name_kh;
        categorySelect.appendChild(option);
    });
}

function showConfirmationModal(action, id) {
    document.getElementById("confirmation-modal").style.display = "block";
    const modalText = document.getElementById("confirm-modal-text");
    if (action === 'create') {
//        modalText.textContent = 'Are you sure you want to create a new product?';
        modalText.textContent = 'តើអ្នកប្រាកដថាចង់បញ្ជូលផលិតផលថ្មីមែនទេ?';
    } else if (action === 'update') {
//        modalText.textContent = 'Are you sure you want to update this product?';
        modalText.textContent = 'តើអ្នកប្រាកដថាចង់ធ្វើការកែប្រែផលិតផលនេះទេ?';
    } else if (action === 'delete') {
//        modalText.textContent = 'Are you sure you want to delete this product?';
        modalText.textContent = 'តើអ្នកប្រាកដថាចង់ធ្វើការលុបផលិតផលនេះទេ?';
    }
    window.currentAction = action;
    window.productId = id;
}
function closeConfirmationModal() {
    document.getElementById("confirmation-modal").style.display = "none";
}

function confirmProductActionConfirmation() {
    if (window.currentAction === 'create') {
        createNewProduct();
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

    if(titleText.includes("បញ្ជូលផលិតថ្មី")) {
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