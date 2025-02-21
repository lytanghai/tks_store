window.addEventListener('load', function() {
})

let globalAction = 'Create';

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
        id: document.getElementById("id").value,
        name: document.getElementById("name").value,
        name_kh: document.getElementById("name_kh").value,
        description: document.getElementById("description").value
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
        sessionStorage.setItem('popupMessage', 'success');
        sessionStorage.setItem('popupAction', 'update');
        showPopUpMessage('success', 'update');
    })
    .catch(error => {
        showPopUpMessage('error', 'update');
    });
}

function openCreateCategoryModal(element) {
    alert('openCreateCategoryModal:: ' + globalAction)
    let id = element.getAttribute("id");
    let name = element.getAttribute("data-name");
    let nameKh = element.getAttribute("data-name-kh");
    let description = element.getAttribute("data-description");

    document.getElementById("id_edit").value = id;
    document.getElementById("name_edit").value = name;
    document.getElementById("name_kh_edit").value = nameKh;
    document.getElementById("description_edit").value = description;

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

function openCreateCategoryModal(element) {
    alert('openCreateCategoryModal:: ' + globalAction)
    let id = element.getAttribute("id");
    let name = element.getAttribute("data-name");
    let nameKh = element.getAttribute("data-name-kh");
    let description = element.getAttribute("data-description");

    var iconElement = document.querySelector(".icon-service-type");
    var formTitle = element.getAttribute("data-form-title");

    let spanElement = element.querySelector("span");
    let newTitle;
    if(formTitle === 'Create') {
        document.getElementById('categoryIdFieldGroup').style.display = 'block';
        document.getElementById("category-back").style.display = 'none';
        document.getElementById("form-modal-category-title").textContent = 'បញ្ជូលប្រភេទទំនិញថ្មី';
        document.getElementById("category-edit-btn").textContent = 'បញ្ជូល';
        iconElement.src = "/icon/new-product-icon.png";
        iconElement.alt = "new-product-icon.png";
        newTitle = "Update";
        element.setAttribute("data-form-title", "Update"); // Change the value
        document.getElementById("id").value = '';
        document.getElementById("name").value = '';
        document.getElementById("name_kh").value = '';
        document.getElementById("description").value = '';
        spanElement.textContent = "កែ";
        globalAction = 'Create';

    } else if(formTitle == 'Update') {
        document.getElementById('categoryIdFieldGroup').style.display = 'block';
        document.getElementById("form-modal-category-title").textContent = 'កែប្រែប្រភេទទំនិញចាស់';
        document.getElementById("category-edit-btn").textContent = 'កែប្រែ';
        iconElement.src = "/icon/edit-product-icon.png";
        iconElement.alt = "edit-product-icon.png";
        newTitle = "Create";
        element.setAttribute("data-form-title", "Create");
        document.getElementById("id").value = id;
        document.getElementById("name").value = name;
        document.getElementById("name_kh").value = nameKh;
        document.getElementById("description").value = description;
        document.getElementById("category-back").style.display = 'block';
        element.setAttribute("data-form-title", newTitle);
        globalAction = 'Update';
    }
}

function resetToCreateMode(element) {
    globalAction = 'Create';
    alert('resetToCreateMode:: ' + globalAction)

    let allElements = document.querySelectorAll('.category-edit-href');
    // Re-enable clicking on all elements
    allElements.forEach(function(el) {
        el.style.pointerEvents = 'auto'; // Re-enable clicking
        el.style.cursor = 'pointer'; // Change the cursor back to 'pointer'
    });
    document.getElementById("createCategory").reset();

    var iconElement = document.querySelector(".icon-service-type");
    var formTitle = element.getAttribute("data-form-title");
    let spanElement = element.querySelector("span");
    globalAction = formTitle;
    let newTitle;
    if(formTitle === 'Create') {
        document.getElementById('categoryIdFieldGroup').style.display = 'block';
        document.getElementById("category-back").style.display = 'none';
        document.getElementById("id").style.display = 'none';
        document.getElementById("category-form-ipt-id").style.display = 'none';
        document.getElementById("form-modal-category-title").textContent = 'បញ្ជូលប្រភេទទំនិញថ្មី';
        document.getElementById("category-edit-btn").textContent = 'បញ្ជូល';
        iconElement.src = "/icon/new-product-icon.png";
        iconElement.alt = "new-product-icon.png";
        newTitle = "Update";
        element.setAttribute("data-form-title", "Update");
        document.getElementById("id").value = '';
        document.getElementById("name").value = '';
        document.getElementById("name_kh").value = '';
        document.getElementById("description").value = '';
        spanElement.textContent = "កែ";
        globalAction = 'Create';
    } else {
        globalAction = 'Update';
    }
}

function openUpdateCategoryModal(element) {
    globalAction = 'Update';

    element.style.pointerEvents = 'none'; // Disable clicking
    element.style.cursor = 'not-allowed'; // Change the cursor to 'not-allowed'

        // Disable clicking on all other elements with the same class
        let allElements = document.querySelectorAll('.category-edit-href');
        allElements.forEach(function(el) {
            if (el !== element) {
                el.style.pointerEvents = 'none'; // Disable clicking
                el.style.cursor = 'not-allowed'; // Change the cursor to 'not-allowed'
            }
        });

    alert('openUpdateCategoryModal:: ' + globalAction);


    let id = element.getAttribute("id");
    let name = element.getAttribute("data-name");
    let nameKh = element.getAttribute("data-name-kh");
    let description = element.getAttribute("data-description");

    var iconElement = document.querySelector(".icon-service-type");
    var formTitle = element.getAttribute("data-form-title");

    let spanElement = element.querySelector("#category-edit-btn");
    if(formTitle === 'Create') {
        document.getElementById("category-back").style.display = 'none';
        document.getElementById('categoryIdFieldGroup').style.display = 'block';
        document.getElementById("form-modal-category-title").textContent = 'បញ្ជូលប្រភេទទំនិញថ្មី';
        document.getElementById("category-edit-btn").textContent = 'បញ្ជូល';
        iconElement.src = "/icon/new-product-icon.png";
        iconElement.alt = "new-product-icon.png";
        element.setAttribute("data-form-title", "Update"); // Change the value
        document.getElementById("id").value = '';
        document.getElementById("name").value = '';
        document.getElementById("name_kh").value = '';
        document.getElementById("description").value = '';
        spanElement.textContent = "កែ";
        globalAction = 'Create';

    } else if(formTitle == 'Update') {
        document.getElementById('categoryIdFieldGroup').style.display = 'block';
        document.getElementById("form-modal-category-title").textContent = 'កែប្រែប្រភេទទំនិញចាស់';
        document.getElementById("category-edit-btn").textContent = 'កែប្រែ';
        iconElement.src = "/icon/edit-product-icon.png";
        iconElement.alt = "edit-product-icon.png";
        element.setAttribute("data-form-title", "Create");
        document.getElementById("id").value = id;
        document.getElementById("name").value = name;
        document.getElementById("name_kh").value = nameKh;
        document.getElementById("description").value = description;
        document.getElementById("category-back").style.display = 'block';
        globalAction = 'Update';
    }
}

function showCategoryConfirmationModal(action, id) {
    alert('showCategoryConfirmationModal:: ' + globalAction);
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
    alert('closeConfirmationCategoryModal:: ' + globalAction);
    document.getElementById("category-confirmation-modal").style.display = "none";
}

function confirmCategoryActionConfirmation() {
    alert('confirmCategoryActionConfirmation:: ' + globalAction);

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
    alert('handleCategoryFormSubmit:: ' + globalAction);

    event.preventDefault();
    if(globalAction === 'Create') {
        showCategoryConfirmationModal('create');
    } else {
        showCategoryConfirmationModal('update');
    }
}
