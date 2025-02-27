let categoryGlobalAction = 'Create';

function clearCategoryModalInput() {
//    document.getElementById("category_name").value = '';
//    document.getElementById("category_name_kh").value = '';
//    document.getElementById("category_description").value = '';
}

function createNewCategory() {
    var name = document.getElementById('category_name').value;
    var nameKh = document.getElementById('category_name_kh').value;
    var description = document.getElementById('category_description').value;

    var formData = {
        name: name,
        name_kh: nameKh,
        description: description
    };

    fetch('/api/category/create', {
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

function deleteCategory(id) {
    fetch('/api/category/delete', {
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

function updateCategory() {
    let formData = {
        id: document.getElementById("category_id").value,
        name: document.getElementById("category_name").value,
        name_kh: document.getElementById("category_name_kh").value,
        description: document.getElementById("category_description").value
    };

    fetch('/api/category/update', {
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

function categoryFilterResults() {
    let searchValue = document.getElementById("search_input_category").value.toLowerCase();
    let startDate = document.getElementById("categoryStartDate").value;
    let endDate = document.getElementById("categoryEndDate").value;
    let rows = document.querySelectorAll("#categoryTable tr");

    rows.forEach(row => {
        let rowText = row.textContent.toLowerCase();
        let rowDate = row.cells[5].textContent.trim();
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

function clearCategoryStartDate() {
    document.getElementById("categoryStartDate").value = "";
    categoryFilterResults();
}

function clearCategoryEndDate() {
    document.getElementById("categoryEndDate").value = "";
        categoryFilterResults();
}

let num = 0;
function resetToCreateCategoryMode(element) {
    categoryGlobalAction = 'Create';
//    alert('resetToCreateCategoryMode: Create')

    let allElements = document.querySelectorAll('.category-edit-href');
    allElements.forEach(function(el) {
        el.style.pointerEvents = 'auto';
        el.style.cursor = 'pointer';
    });
    document.getElementById("createCategory").reset();
    var iconElement = document.querySelector(".icon-service-type");
    let spanElement = element.querySelector("span");

    document.getElementById('categoryIdFieldGroup').style.display = 'none';
    document.getElementById("category-back").style.display = 'none';
    document.getElementById("category_id").style.display = 'none';
    document.getElementById("category-form-ipt-id").style.display = 'none';
    document.getElementById("form-modal-category-title").textContent = 'បញ្ជូលប្រភេទទំនិញថ្មី';
    document.getElementById("category-edit-btn").textContent = 'បញ្ជូល';
    iconElement.src = "/icon/new-product-icon.png";
    iconElement.alt = "new-product-icon.png";
    element.setAttribute("data-form-title", "Update");
    document.getElementById("category_id").value = '';
    document.getElementById("category_name").value = '';
    document.getElementById("category_name_kh").value = '';
    document.getElementById("category_description").value = '';
    spanElement.textContent = "បញ្ជូល";
    window.currentAction = 'Create';
    categoryGlobalAction = 'Update';
}

function resetToUpdateCategoryMode(element) {
    categoryGlobalAction = 'Update';
//    alert('resetToUpdateCategoryMode: Update')
    let allElements = document.querySelectorAll('.category-edit-href');
    allElements.forEach(function(el) {
        el.style.pointerEvents = 'auto';
        el.style.cursor = 'pointer';
    });

    let id = element.getAttribute("id");
    let name = element.getAttribute("data-name");
    let nameKh = element.getAttribute("data-name-kh");
    let description = element.getAttribute("data-description");
    let spanElement = element.querySelector("span");

    var iconElement = document.querySelector(".icon-service-type");

    document.getElementById("createCategory").reset();
    document.getElementById('categoryIdFieldGroup').style.display = 'none';
    document.getElementById("category-back").style.display = 'block';
    document.getElementById("category_id").style.display = 'block';
    document.getElementById("category-form-ipt-id").style.display = 'block';
    document.getElementById("form-modal-category-title").textContent = 'កែប្រភេទទំនិញចាស់';
    document.getElementById("category-edit-btn").textContent = 'កែប្រែ';
    iconElement.src = "/icon/edit-product-icon.png";
    iconElement.alt = "edit-product-icon.png";
    element.setAttribute("data-form-title", "Update");
    document.getElementById("category_id").value = id;
    document.getElementById("category_name").value = name;
    document.getElementById("category_name_kh").value = nameKh;
    document.getElementById("category_description").value = description;
    spanElement.textContent = "កែ";
    categoryGlobalAction = 'Create';
    window.currentAction = 'Update';
}

function showCategoryConfirmationModal(action, id) {
//    alert('showCategoryConfirmationModal: ' + action)
    document.getElementById("category-confirmation-modal").style.display = "block";
    const modalText = document.getElementById("category-confirm-modal-text");
    if (action === 'create') {
        modalText.textContent = 'តើអ្នកប្រាកដថាចង់បញ្ជូលប្រភេទទំនិញថ្មីមែនទេ?';
    } else if (action === 'update') {
        modalText.textContent = 'តើអ្នកប្រាកដថាចង់ធ្វើការកែប្រែប្រភេទទំនិញនេះទេ?';
    } else if (action === 'delete') {
        modalText.textContent = 'តើអ្នកប្រាកដថាចង់ធ្វើការលុបប្រភេទទំនិញនេះទេ?';
    }
    window.currentAction = action;
    window.categoryId = id;
}

function closeConfirmationCategoryModal() {
    document.getElementById("category-confirmation-modal").style.display = "none";
}

function confirmCategoryActionConfirmation() {
//    alert('confirmCategoryActionConfirmation ' + window.currentAction)

    if (window.currentAction === 'create') {
        createNewCategory();
    } else if (window.currentAction === 'update') {
        updateCategory();
    } else if (window.currentAction === 'delete') {
        deleteCategory(window.categoryId);
    }
    updateServiceIcon();
    closeConfirmationCategoryModal();
}

function handleCategoryFormSubmit(event) {
    event.preventDefault();
//    alert('handleCategoryFormSubmit ' + document.getElementById("category-edit-btn").textContent)
    if(document.getElementById("category-edit-btn").textContent === 'បញ្ជូល') {
        showCategoryConfirmationModal('create');
    } else {
        showCategoryConfirmationModal('update');
    }
}
