window.addEventListener('load', function() {
    closeCreateCategoryModal();
})

function createNewCategory() {
    var name = document.getElementById('name').value;
    var nameKh = document.getElementById('name_kh').value;
    var description = document.getElementById('description').value;

    var formData = {
        name: name,
        name_kh: nameKh,
        description: description
    };

    fetch('/category/create', {
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

    fetch('/category/delete', {
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
        id: document.getElementById("id_edit").value,
        name: document.getElementById("name_edit").value,
        name_kh: document.getElementById("name_kh_edit").value,
        description: document.getElementById("description_edit").value
    };

    fetch('/category/update', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
    .then(response => response.json())
    .then(data => {
        document.getElementById("myModal").style.display = "none";
        sessionStorage.setItem('popupMessage', 'success');
        sessionStorage.setItem('popupAction', 'update');
        showPopUpMessage('success', 'update');
    })
    .catch(error => {
        showPopUpMessage('error', 'update');
    });
}

function openCreateCategoryModal(element) {

    let id = element.getAttribute("id");
    let name = element.getAttribute("data-name");
    let nameKh = element.getAttribute("data-name-kh");
    let description = element.getAttribute("data-description");

    document.getElementById("id_edit").value = id;
    document.getElementById("name_edit").value = name;
    document.getElementById("name_kh_edit").value = nameKh;
    document.getElementById("description_edit").value = description;

    document.getElementById("myModal").style.display = "block";
}

function closeCreateCategoryModal() {
    document.getElementById("myModal").style.display = "none";
}

function filterResults() {
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
    filterResults();
}

function clearCategoryEndDate() {
    document.getElementById("categoryEndDate").value = "";
    filterResults();
}

function openUpdateCategoryModal(element) {

    let id = element.getAttribute("id");
    let name = element.getAttribute("data-name");
    let nameKh = element.getAttribute("data-name-kh");
    let description = element.getAttribute("data-description");

    var iconElement = document.querySelector(".icon-service-type");
    var formTitle = element.getAttribute("data-form-title");

    if(formTitle === 'Create') {
        document.getElementById("form-modal-category-title").textContent = 'បញ្ជូលប្រភេទទំនិញថ្មី';
        document.getElementById("category-edit-btn").textContent = 'បញ្ជូល';
        iconElement.src = "/icon/new-product-icon.png";
        iconElement.alt = "new-product-icon.png";
    } else if(formTitle == 'Update') {
        document.getElementById("form-modal-category-title").textContent = 'កែប្រែទិន្ន័យប្រភេទផលិតផលចាស់';
        document.getElementById("category-edit-btn").textContent = 'កែប្រែ';
        iconElement.src = "/icon/edit-product-icon.png";
        iconElement.alt = "edit-product-icon.png";
    }

    document.getElementById("id_edit").value = id;
    document.getElementById("name_edit").value = name;
    document.getElementById("name_kh_edit").value = nameKh;
    document.getElementById("description_edit").value = description;
    document.getElementById("myModal").style.display = "block";
}

function showCategoryConfirmationModal(action, id) {
    document.getElementById("category-confirmation-modal").style.display = "block";
    const modalText = document.getElementById("category-confirm-modal-text");
    if (action === 'create') {
        modalText.textContent = 'តើអ្នកប្រាកដថាចង់បញ្ជូលប្រភេទផលិតផលថ្មីមែនទេ?';
    } else if (action === 'update') {
        modalText.textContent = 'តើអ្នកប្រាកដថាចង់ធ្វើការកែប្រែប្រភេទផលិតផលនេះទេ?';
    } else if (action === 'delete') {
        modalText.textContent = 'តើអ្នកប្រាកដថាចង់ធ្វើការលុបប្រភេទផលិតផលនេះទេ?';
    }
    window.currentAction = action;
    window.categoryId = id;
}

function closeConfirmationCategoryModal() {
    document.getElementById("category-confirmation-modal").style.display = "none";
}

function confirmCategoryActionConfirmation() {
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
    const titleText = document.getElementById("form-modal-category-title").textContent.toLowerCase();
    if(titleText.includes("បញ្ជូលប្រភេទទំនិញថ្មី")) {
        showCategoryConfirmationModal('create');
    } else {
        showCategoryConfirmationModal('update');
    }
}
