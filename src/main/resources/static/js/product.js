function createNewProduct() {
    var name = document.getElementById('name_en').value;
    var nameKh = document.getElementById('name_kh').value;
    var categoryId = document.getElementById('category_id').value;
    var salePrice = document.getElementById('sale_price').value;
    var currency = document.getElementById('currency').value;
    var description = document.getElementById('description').value;

    var formData = {
        name_en: nameEn,
        name_kh: nameKh,
        category_id: categoryId,
        sale_price: salePrice,
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

function deleteProduct(element) {
    var productId = element.id;

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

function updateProduct(event) {
    event.preventDefault();

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

function openCreateProductModal(element) {

    let id = element.getAttribute("id");
    let nameEn = element.getAttribute("data-name-en");
    let nameKh = element.getAttribute("data-name-kh");
    let currency = element.getAttribute("data-currency");
    let salePrice = element.getAttribute("data-sale-price");
    let description = element.getAttribute("data-description");

    document.getElementById("id_edit").value = id;
    document.getElementById("name_en_edit").value = nameEn;
    document.getElementById("name_kh_edit").value = nameKh;
    document.getElementById("description_edit").value = description;
    document.getElementById("currency_edit").value = currency;
    document.getElementById("sale_price_edit").value = salePrice;


    document.getElementById("myProductModal").style.display = "block";
}

function closeCreateProductModal() {
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

function clearStartDate() {
    document.getElementById("productStartDate").value = "";
    filterResults();
}

function clearEndDate() {
    document.getElementById("productEndDate").value = "";
    filterResults();
}