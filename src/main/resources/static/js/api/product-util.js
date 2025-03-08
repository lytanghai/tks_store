let currentPage = 0;
let totalPage = 0;
const buttonBackPage = document.getElementById("back-page");
const buttonToPage = document.getElementById("to-page");
if (buttonBackPage) {
    buttonBackPage.addEventListener("click", function () {
        if (currentPage >= 1) {
            currentPage -= 1;
            fetchFilterProduct();
        }
    });
} else {
    console.error("Button with id 'back-page' not found.");
}

if (buttonToPage) {
        buttonToPage.addEventListener("click", function () {
        if(currentPage <= totalPage - 2) {
            currentPage += 1;
            fetchFilterProduct();
        }
    });
} else {
    console.error("Button with id 'to-page' not found.");
}
function fetchFilterProduct() {

    document.getElementById("back-page-init-container").style.display = 'none';
    document.getElementById("to-page-init-container").style.display = 'none';
    document.getElementById("back-page-container").style.display = 'block';
    document.getElementById("to-page-container").style.display = 'block';

    const condition = document.getElementById("filterCondition").value;
    const searchProperty = document.getElementById("filterColumn").value;
    const searchGeneralValue = document.getElementById("search_input_product").value;
    let searchValue = document.getElementById("filterValue") ? document.getElementById("filterValue").value : "";
    let searchValue1 = document.getElementById("filterValue1") ? document.getElementById("filterValue1").value : "";
    let searchValue2 = document.getElementById("filterValue2") ? document.getElementById("filterValue2").value : "";
    let url = '/internal/product/list/filter?condition_type=' + condition + '&' + searchProperty + '=' + searchValue + '&page=' + currentPage;
    let salePriceCurrencyValue = document.getElementById("product_filter_search_currency").value;
    if(searchProperty === 'defaultChoice') {
        //when open product tab
        url = '/internal/product/list/filter?condition_type=' + condition + searchValue + "&general=" + searchGeneralValue + '&page=' + currentPage;
    } else {
        if(salePriceCurrencyValue !== 'none') {
            url = '/internal/product/list/filter?condition_type=' + condition + '&' + searchProperty + '=' + searchValue + '&sale_price_currency=' + salePriceCurrencyValue + '&page=' + currentPage;
        } else {
            url = '/internal/product/list/filter?condition_type=' + condition + '&' + searchProperty + '=' + searchValue + '&page=' + currentPage;
        }
    }
    console.log('url: ' + url)
    fetch(url)
        .then(response => response.json())
        .then(data => {
            const productTable = document.getElementById("productTable");
            totalPage = data.totalPages;
            productTable.innerHTML = '';

            data.products.forEach(product => {
                const row = document.createElement("tr");

                row.innerHTML = `
                    <td class="table_td">${product.code}</td>
                    <td class="table_td">${product.name_kh ? product.name_en + ' (' + product.name_kh + ')' : product.name_en}</td>
                    <td class="table_td">${product.category.name_kh ? product.category.name + ' (' + product.category.name_kh + ')' : product.category.name}</td>
                    <td class="table_td custom-sale-price">
                        ${formatCurrency(product.sale_price, product.currency)}
                    </td>
                    <td>
                        <ul>
                            ${product.variants.map(variant => `<li>${variant.sku}</li>`).join('')}
                        </ul>
                    </td>
                    <td class="custom-stock-qty">
                        <ul style="text-align:center">
                            ${product.variants.map(variant => `<li>${variant.stock_quantity}</li>`).join('')}
                        </ul>
                    </td>
                    <td class="view_img_td">
                        <ul>
                            ${product.variants.map(variant => `<li>
                                <button class="view-image-btn" data-variant-id="${variant.id}" onclick="openVariantAttributeDetail(this)">⚙️</button>
                            </li>`).join('')}
                        </ul>
                    </td>
                    <td class="view_img_td custom-image">
                        <ul>
                            ${product.variants.map(variant => `<li>
                                <button class="view-image-btn" data-variant-id="${variant.id}" onclick="openImageSlider(this)">🔍</button>
                            </li>`).join('')}
                        </ul>
                    </td>
                    <td style="font-size: 1rem">
                        <ul class="product-action">
                            <li>
                                <a id="${product.id}" href="#" onclick="showConfirmationModal('delete', ${product.id})">
                                    <span class="unicode-icon">&#128465;លុប</span>
                                </a>
                            </li>
                            <li> <span style="font-size:1.3rem">|</span> </li>
                            <li>
                                <a id="${product.id}" data-code="${product.code}"
                                    data-categoryEn="${product.category.name}"
                                    data-categoryKh="${product.category.name_kh}"
                                    data-name-en="${product.name_en}"
                                    data-name-kh="${product.name_kh}"
                                    data-sale-price="${product.sale_price}"
                                    data-currency="${product.currency}"
                                    data-description="${product.description}"
                                    data-base-price="${product.variants[0]?.base_price || 0}"
                                    data-base-price-currency="${product.variants[0]?.base_price_currency || ''}"
                                    data-stock-quantity="${product.variants[0]?.stock_quantity || 0}"
                                    data-sku="${product.variants[0]?.sku || ''}"
                                    data-images="${product.variants[0]?.images.map(image => image.id + ':' + image.uuid).join(',') || ''}"
                                    data-attributes="${product.variants[0]?.attributes.map(attribute => attribute.name + ':' + attribute.value).join(',') || ''}"
                                    data-form-title="Update"
                                    href="#" onclick="openCreateUpdateProductModal(this)">
                                    <span class="unicode-icon">&#x270E;កែ</span>
                                </a>
                            </li>
                        </ul>
                    </td>
                `;
                productTable.appendChild(row);
            });
        })
        .catch(error => console.error("Error fetching data:", error));
}

function formatCurrency(amount, currency) {
    if (currency === 'USD') {
        return `${amount.toFixed(2)} ដុល្លារ = ${(amount * 4100).toFixed(0)} រៀល`;
    } else if (currency === 'KHR') {
        return `${(amount / 4100).toFixed(2)} ដុល្លារ = ${amount.toFixed(0)} រៀល`;
    }
    return `${amount} ${currency}`;
}

function resetFilter() {
    document.getElementById("filterCondition").value = "";
    document.getElementById("filterColumn").value = "";
    if (document.getElementById("filterValue")) document.getElementById("filterValue").value = "";
    if (document.getElementById("filterValue1")) document.getElementById("filterValue1").value = "";
    if (document.getElementById("filterValue2")) document.getElementById("filterValue2").value = "";

    currentPage = 0;

    fetchFilterProduct();
}

document.getElementById("search_input_product").addEventListener("keyup", function() {
    setTimeout(fetchFilterProduct, 1500);
})
