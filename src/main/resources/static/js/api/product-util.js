if(window.location.pathname.includes("/api/product")) {

    const buttonBackPage = document.getElementById("back-page");
    const buttonToPage = document.getElementById("to-page");
    if (buttonBackPage) {
        buttonBackPage.addEventListener("click", function () {
            if (currentPage > 1) {
                currentPage--;
                fetchFilterProduct();
            }
        });
    } else {
        console.error("Button with id 'back-page' not found.");
    }

    if (buttonToPage) {
            buttonToPage.addEventListener("click", function () {
            if(currentPage < totalPage) {
                currentPage++;
                fetchFilterProduct();
            }
        });
    } else {
        console.error("Button with id 'to-page' not found.");
    }

    function fetchFilterProduct() {
        const loadingSpinner = document.getElementById("loading-spinner");
        loadingSpinner.style.display = "block";

        const loadingSpinner2 = document.getElementById("loading-spinner_2");
        loadingSpinner2.style.display = "block";

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
        if(searchGeneralValue !== '') {
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
        setTimeout(() => {
            fetch(url)
                    .then(response => response.json())
                    .then(data => {
                        const productTable = document.getElementById("productTable");
                        totalPage = data.totalPages;
                        document.getElementById("display-page-num").textContent = currentPage;
                        document.getElementById("display-page-total").textContent = totalPage;

                        productTable.innerHTML = '';

                        data.products.forEach(product => {
                            const row = document.createElement("tr");
            //                        <td class="product_td">${formatDate(product.created_at)}</td> <!-- Format Date here -->
                            row.innerHTML = `
                                <td class="table_td">${product.code}</td>
                                <td class="table_td">${product.name_kh ? product.name_en + ' (' + product.name_kh + ')' : product.name_en}</td>
                                <td class="table_td">${product.category.name_kh ? product.category.name + ' (' + product.category.name_kh + ')' : product.category.name}</td>
                                <td class="table_td custom-sale-price">
                                    ${formatCurrency(product.sale_price, product.currency)}
                                </td>

                                <td class="custom-stock-qty">
                                            <ul style="text-align:center">
                                                ${product.variants.map(variant => `
                                                    <li style="color: ${variant.stock_quantity < 10 ? 'red' : 'black'}; font-weight: normal;">
                                                        ${variant.stock_quantity}
                                                    </li>
                                                `).join('')}
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
//                                                data-sku="${product.variants[0]?.sku || ''}"
                                                data-images="${product.variants[0]?.images.map(image => image.id + ':' + image.uuid).join(',') || ''}"
                                                data-attributes="${product.variants[0]?.attributes.map(attribute => attribute.id + ':' + attribute.name + ':' + attribute.value + ':' + attribute.group_num).join(',') || ''}"
                                                data-variantId="${product.variants[0]?.id || ''}"
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
                    .catch(error => console.error("Error fetching data:", error))
                    .finally(() => {
                        loadingSpinner.style.display = "none";
                        loadingSpinner2.style.display = "none";
                    });
        }, 500)
    }

    function formatDate(dateString) {
        const date = new Date(dateString); // Convert to Date object
        const year = date.getFullYear();
        const month = (date.getMonth() + 1).toString().padStart(2, '0'); // Month is 0-indexed
        const day = date.getDate().toString().padStart(2, '0'); // Pad day with leading zero if necessary
        return `${year}-${month}-${day}`; // Return formatted date
    }

    function formatCurrency(amount, currency) {
        // Format the number with commas every 3 digits and 2 decimal places for USD
        const formattedAmount = new Intl.NumberFormat('en-US', {
            minimumFractionDigits: currency === 'KHR' ? 0 : 2,
            maximumFractionDigits: currency === 'KHR' ? 0 : 2
        }).format(amount);

        // Convert USD to Riel and format without decimals
        const formattedRielAmount = new Intl.NumberFormat('en-US', {
            minimumFractionDigits: 0,
            maximumFractionDigits: 0
        }).format(amount * 4100);

        if (currency === 'USD') {
            return `${formattedAmount} $ = ${formattedRielAmount} ៛`;
        } else if (currency === 'KHR') {
            const formattedUsdAmount = new Intl.NumberFormat('en-US', {
                minimumFractionDigits: 2,
                maximumFractionDigits: 2
            }).format(amount / 4100); // Format USD with 2 decimals
            return `${formattedUsdAmount} $ = ${formattedAmount} ៛`;
        }
        return `${formattedAmount} ${currency}`;
    }

    function resetFilter() {
        document.getElementById("filterCondition").value = "defaultCondition";
        document.getElementById("filterColumn").value = "defaultChoice";
        if (document.getElementById("filterValue")) document.getElementById("filterValue").value = "";
        if (document.getElementById("filterValue1")) document.getElementById("filterValue1").value = "";
        if (document.getElementById("filterValue2")) document.getElementById("filterValue2").value = "";
        currentPage = 1;

        fetchFilterProduct();
    }

    document.getElementById("search_input_product").addEventListener("keyup", function () {
        clearTimeout(searchTimeout); // Cancel previous timeout

        searchTimeout = setTimeout(() => {
            fetchFilterProduct();
        }, 1500);
    });

    document.getElementById("btn-submit-filter").addEventListener("click", function () {
        if(document.getElementById("filterColumn").value === 'defaultChoice' || document.getElementById("filterCondition").value === 'defaultCondition') {
            showAlertMessageModal('ERROR!','សូមធ្វើការជ្រើសរើសតម្លៃ និងលក្ខខណ្ឌដើម្បីស្វែងរក!')
           return;
        }else {
            if(document.getElementById("filterCondition").value.includes("EQUAL","CONTAINS","GREATER_THAN", "LESS_THAN")) {
                if(document.getElementById("filterValue").value === '') {
                   showAlertMessageModal('ERROR!','សូមធ្វើការបញ្ជូលតម្លៃដើម្បីស្វែងរក!')
                   return;
                }
            } else {
                if(document.getElementById("filterCondition").value === 'BETWEEN' && document.getElementById("filterValue1").value === '' && document.getElementById("filterValue2").value === '') {
                    showAlertMessageModal('ERROR!','សូមធ្វើការបញ្ជូលតម្លៃដើម្បីស្វែងរក!')
                    return;
                }
            }
            fetchFilterProduct();
        }
    })
}