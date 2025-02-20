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

function deleteCategory(element) {
    var categoryId = element.id;

    fetch('/category/delete', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ id: categoryId })
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

function updateCategory(event) {
    event.preventDefault();

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