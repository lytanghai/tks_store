let attributeGlobalAction = 'Create';

function clearAttributeModalInput() {
    document.getElementById("attribute_name").value = '';
    document.getElementById("attribute_name_kh").value = '';
}

function createNewAttribute() {
    var name = document.getElementById('attribute_name').value;
    var nameKh = document.getElementById('attribute_name_kh').value;
    var formData = {
        name: name,
        name_kh: nameKh
    };

    fetch('/attribute/create', {
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

function deleteAttribute(id) {
    fetch('/attribute/delete', {
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

function updateAttribute() {
    let formData = {
        id: document.getElementById("attribute_id").value,
        name: document.getElementById("attribute_name").value,
        name_kh: document.getElementById("attribute_name_kh").value,
    };

    fetch('/attribute/update', {
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

function filterResults() {
    let searchValue = document.getElementById("search_input_attribute").value.toLowerCase();
    let rows = document.querySelectorAll("#attributeTable tr");
    rows.forEach(row => {
        let rowText = row.textContent.toLowerCase();
        let showRow = rowText.includes(searchValue);
        row.style.display = showRow ? "" : "none";
    });
}

function resetToCreateAttributeMode(element) {
    attributeGlobalAction = 'Create';
    let allElements = document.querySelectorAll('.attribute-edit-href');
    allElements.forEach(function(el) {
        el.style.pointerEvents = 'auto';
        el.style.cursor = 'pointer';
    });
    var iconElement = document.querySelector(".icon-service-type");
    let spanElement = element.querySelector("span");

    document.getElementById("createAttribute").reset();
    document.getElementById('attributeIdFieldGroup').style.display = 'block';
    document.getElementById("attribute-back").style.display = 'none';
    document.getElementById("attribute_id").style.display = 'none';
    document.getElementById("attribute-form-ipt-id").style.display = 'none';
    document.getElementById("form-modal-attribute-title").textContent = 'បញ្ជូលអង្គធាតុទំនិញថ្មី';
    document.getElementById("attribute-edit-btn").textContent = 'បញ្ជូល';
    document.getElementById("attribute_id").value = '';
    document.getElementById("attribute_name").value = '';
    document.getElementById("attribute_name_kh").value = '';
    iconElement.src = "/icon/new-product-icon.png";
    iconElement.alt = "new-product-icon.png";
    element.setAttribute("data-form-title", "Update");
    spanElement.textContent = "បញ្ជូល";
    window.currentAction = 'Create';
}

function resetToUpdateAttributeMode(element) {
    attributeGlobalAction = 'Update';
    let allElements = document.querySelectorAll('.attribute-edit-href');
    allElements.forEach(function(el) {
        el.style.pointerEvents = 'auto';
        el.style.cursor = 'pointer';
    });
    document.getElementById("createAttribute").reset();
    let id = element.getAttribute("attribute-id");
    let name = element.getAttribute("data-attribute-name");
    let nameKh = element.getAttribute("data-attribute-name-kh");

    var iconElement = document.querySelector(".icon-service-type");

    let spanElement = element.querySelector("span");

    document.getElementById('attributeIdFieldGroup').style.display = 'none';
    document.getElementById("attribute-back").style.display = 'block';
    document.getElementById("attribute_id").style.display = 'block';
    document.getElementById("attribute-form-ipt-id").style.display = 'block';
    document.getElementById("form-modal-attribute-title").textContent = 'កែប្រែអង្គធាតុទំនិញចាស់';
    document.getElementById("attribute-edit-btn").textContent = 'កែប្រែ';
    document.getElementById("attribute_id").value = id;
    document.getElementById("attribute_name").value = name;
    document.getElementById("attribute_name_kh").value = nameKh;
    iconElement.src = "/icon/edit-product-icon.png";
    iconElement.alt = "edit-product-icon.png";
    element.setAttribute("data-attribute-form-title", "Update");
    spanElement.textContent = "កែ";
    attributeGlobalAction = 'Create';
    window.currentAction = 'Update';
}

function showAttributeConfirmationModal(action, id) {
    const modalText = document.getElementById("attribute-confirm-modal-text");
    if (action === 'create') {
        modalText.textContent = 'តើអ្នកប្រាកដថាចង់បញ្ជូលអង្គធាតុទំនិញមែនទេ?';
    } else if (action === 'update') {
        modalText.textContent = 'តើអ្នកប្រាកដថាចង់ធ្វើការកែប្រែអង្គធាតុទំនិញនេះ?';
    } else if (action === 'delete') {
        modalText.textContent = 'តើអ្នកប្រាកដថាចង់ធ្វើការលុបអង្គធាតុទំនិញនេះទេ?';
    }
    document.getElementById("attribute-confirmation-modal").style.display = "block";
    window.currentAction = action;
    window.attributeId = id;
}

function closeConfirmationAttributeModal() {
    document.getElementById("attribute-confirmation-modal").style.display = "none";
}

function confirmAttributeActionConfirmation() {
    if (window.currentAction === 'create') {
        createNewAttribute();
    } else if (window.currentAction === 'update') {
        updateAttribute();
    } else if (window.currentAction === 'delete') {
        deleteAttribute(window.attributeId);
    }
    updateServiceIcon();
    closeConfirmationAttributeModal();
}

function handleAttributeFormSubmit(event) {
    event.preventDefault();
    if(document.getElementById("attribute-edit-btn").textContent === 'បញ្ជូល') {
        showAttributeConfirmationModal('create');
    } else {
        showAttributeConfirmationModal('update');
    }
}
