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

    location.reload(true);
}
function openModal() {
    document.getElementById("myModal").style.display = "block";
}

function closeModal() {
    document.getElementById("myModal").style.display = "none";
}