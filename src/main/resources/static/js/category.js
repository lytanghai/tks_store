window.onload = function() {
    closeModal();
};

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
    location.reload(true);
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

    location.reload();
}

function testUpdate() {
    var id = document.getElementById("id_edit").value;
    var name =  document.getElementById("name_edit").value;
    var nameKh = document.getElementById("name_kh_edit").value;
    var desc = document.getElementById("description_edit").value;
}

function submitUpdateCategory(event) {
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
        alert("Category updated successfully!");
        document.getElementById("myModal").style.display = "none";
        location.reload();
    })
    .catch(error => console.error("Error:", error));
}

function openModal(element) {

    let id = element.getAttribute("id");
    let name = element.getAttribute("data-name");
    let nameKh = element.getAttribute("data-name-kh");
    let description = element.getAttribute("data-description");

    document.getElementById("id_edit").value = id;
    document.getElementById("name_edit").value = name;
    document.getElementById("name_kh_edit").value = nameKh;
    document.getElementById("description_edit").value = description;

    document.getElementById("myModal").style.display = "block"; // Show modal
}

function closeModal() {
    document.getElementById("myModal").style.display = "none";
}