let attributeGlobalAction = 'Create';
//1920 x 963

function clearAttributeModalInput() {
//    document.getElementById("attribute_name").value = '';
//    document.getElementById("attribute_name_kh").value = '';
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


function fetchAttributesFilter() {
    let keyword = document.getElementById("search_input_attribute").value;
    let url = keyword === ''
        ? `/internal/attribute/list/filter?page=0`
        : `/internal/attribute/list/filter?page=0&keyword=${encodeURIComponent(keyword)}`;

    // Show loading spinners
    document.getElementById("loading-spinner").style.display = "block";
    document.getElementById("loading-spinner_2").style.display = "block";

    console.log('Fetching URL:', url);

    setTimeout(() => {
        fetch(url)
            .then(response => response.json())
            .then(data => updateAttributeTable(data.totalItems))
            .catch(error => console.error('Error fetching attributes:', error))
            .finally(() => {
                // Hide loading spinners after request completes
                document.getElementById("loading-spinner").style.display = "none";
                document.getElementById("loading-spinner_2").style.display = "none";
            });
    }, 1000); // Add 1-second delay before API call
}


function updateAttributeTable(attributes) {
    const tableBody = document.getElementById("attributeTable");
    tableBody.innerHTML = ""; // Clear existing table rows

    attributes.forEach(attr => {
        const row = document.createElement("tr");
        row.className = attributes.indexOf(attr) % 2 === 0 ? "" : "odd-row";

        row.innerHTML = `
            <td class="attribute_td">${attr.id}</td>
            <td class="attribute_td">${attr.name}</td>
            <td class="attribute_td">${attr.name_kh}</td>
            <td class="attribute_td">${attr.status === 'ACTIVE' ? 'កំពុងដំណើរការ' : attr.status}</td>
            <td style="font-size: 1rem">
                <ul class="attribute-action">
                    <li>
                        <a id="${attr.id}" href="#" onclick="showAttributeConfirmationModal('delete', ${attr.id})">
                            <img src="/icon/trash.png" class="icon" alt="Trash Icon">
                        </a>
                    </li>
                    &nbsp;
                    <li> <span>|</span> </li>
                    &nbsp;
                    <li>
                        <a
                            attribute-id="${attr.id}"
                            data-attribute-name="${attr.name}"
                            data-attribute-name-kh="${attr.name_kh}"
                            data-attribute-form-title="Update"
                            href="#"
                            class="attribute-edit-href"
                            onclick="resetToUpdateAttributeMode(this)">
                            <img src="/icon/edit.png" class="icon" alt="Edit Icon">
                        </a>
                    </li>
                </ul>
            </td>
        `;
        tableBody.appendChild(row);
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
