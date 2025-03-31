document.addEventListener("DOMContentLoaded", () => {
    url = `/internal/product/list/filter?page=${currentStoreProductPage}&size=${itemsPerPage}`;
    fetchFilterProduct(url);
    fetchItems();

    document.getElementById("prevPage").addEventListener("click", () => {
        if (currentStoreProductPage > 1) {
            currentStoreProductPage--;
            url = `/internal/product/list/filter?page=${currentStoreProductPage}&size=${itemsPerPage}`;
            fetchFilterProduct(url);
        }
    });

    document.getElementById("nextPage").addEventListener("click", () => {
        if (currentStoreProductPage < totalStoreProductPage) {
            currentStoreProductPage++;
            url = `/internal/product/list/filter?page=${currentStoreProductPage}&size=${itemsPerPage}`;
            fetchFilterProduct(url);
        }
    });
});

function fetchFilterProduct(url) {
    console.log("request url: " + url)
    clearTimeout(storeDebounceTimeout);

    const loadingSpinner = document.getElementById("loading-spinner");
    loadingSpinner.style.display = "block";

    const loadingSpinner2 = document.getElementById("loading-spinner_2");
    loadingSpinner2.style.display = "block";

    const inputValue = document.getElementById("store-keyword").value;
    const crossTextContainer = document.getElementById("cross-text-container");

    if (inputValue.length > 0) {
        crossTextContainer.style.display = "block";
    } else {
        crossTextContainer.style.display = "none";
    }

    storeDebounceTimeout = setTimeout(() => {
        fetch(url)
            .then(response => response.json())
            .then(data => {
                console.log("API Response:", data);
                if (!data.products || data.products.length === 0) {
                    console.warn("No products found.");
                    displayProducts([]);
                    document.getElementById("store-search-size").textContent = 0;
                    document.getElementById("store-search-datetime").textContent = displayDateTime();
                    document.getElementById("pageIndicator").textContent = `ទំព័រ 1 នៃ 1`;
                    document.getElementById("prevPage").style.display = "none";
                    document.getElementById("nextPage").style.display = "none";
                    return;
                }

                totalStoreProductPage = data.totalPages;
                document.getElementById("pageIndicator").textContent = `ទំព័រ ${currentStoreProductPage} នៃ ${totalStoreProductPage}`;

                document.getElementById("prevPage").disabled = currentStoreProductPage === 1;
                document.getElementById("nextPage").disabled = currentStoreProductPage === totalStoreProductPage;
                document.getElementById("store-search-size").textContent = data.totalItems;
                document.getElementById("store-search-datetime").textContent = displayDateTime();
                displayProducts(data.products);
            })
            .catch(error => {
                console.error("Error fetching data:", error);
                displayProducts([]);
            })
            .finally(() => {
               loadingSpinner.style.display = "none";
               loadingSpinner2.style.display = "none";
            });
    }, 500)

}

function resetFilterTitle() {
    let clearStoreBtn = document.getElementById("clear-store-category");
    if(clearStoreBtn !== '') {
        if(clearStoreFilter) {
            clearStoreBtn.textContent = "𝑪𝒍𝒆𝒂𝒓 | សំអាត";
            clearStoreBtn.style.backgroundColor = "#1e2738";
            clearStoreBtn.style.color = "#fff";
        } else {
            clearStoreBtn.textContent = "𝑺𝒆𝒂𝒓𝒄𝒉 | ស្វែងរកតាមរយះ";
            clearStoreBtn.style.backgroundColor = "rgb(93, 190, 163)";
            clearStoreBtn.style.color = "#000";
        }
    }
}

function truncateText(text, maxLength) {
    return text.length > maxLength ? text.substring(0, maxLength) + "..." : text;
}

function displayProducts(products) {
    const productGrid = document.getElementById("productGrid");
    productGrid.innerHTML = "";

    const centerContent = document.getElementById("center-content");
    if (products.length === 0) {
      centerContent.style.display = "block";
      return;
    }
    centerContent.style.display = "none";

    products.forEach(product => {
        const productDiv = document.createElement("div");
        productDiv.classList.add("grid-item");

        const productNameEn = product.name_en || "";
        const productNameKh = product.name_kh || "";

        let productName = "";
        if(productNameKh === "") {
            productName = truncateText(productNameEn, 25);
        } else {
            productName = truncateText(productNameKh, 25);
        }

        let code = product.code;

        const categoryNameEn = product.category.name || "";
        const categoryNameKh = product.category.name_kh || "";
//        const categoryName = truncateText(categoryNameKh + " | " + categoryNameEn, 30);

       let categoryName = "";
       if(categoryNameKh === "") {
            categoryName = truncateText(categoryNameEn, 25);
       } else {
            categoryName = truncateText(categoryNameKh, 25);
       }

        let productPrice = product.sale_price ? `${product.sale_price} ${product.currency || ""}` : "Price Unavailable";

        productPrice = formatStoreCurrency(product.sale_price, product.currency);

        const variant = product.variants[0]; // Get first variant if available
        let stockQuantity = variant.stock_quantity || "Out of Stock";

        if(stockQuantity < 0) {
            stockQuantity = "Out of Stock";
        }

        const imageUUID = variant?.images?.[0]?.uuid;

        productDiv.innerHTML = `
            <div class="product-card">
                <div class="product-image">
                    <img src="/icon/shopping-cart.png" alt="${productNameEn}">
                </div>
                <div class="product-info">
                    <div class="product-title" title="${productName}">${productName}</div>
                    <div class="product-category" title="${categoryName}">${categoryName}</div>
                    <div class="product-price">${productPrice}</div>
                    <div class="custom-code-container">
                        <div>${code}</div>
                     </div>
                    <div class="product-stock">ស្ដុក:&nbsp;<span style="color: #e28743; font-weight: bold"> ${stockQuantity} </span></div>
                </div>
                <div class="product-actions">
                     <button class="action-btn view store-view-btn"
                        data-product='${JSON.stringify(product)}'>
                        👁️ View
                    </button>
                    <button class="action-btn" onclick="addToCartProduct(${product.id})">🛒 Add to Cart</button>
                </div>
            </div>
        `;

        if (imageUUID) {
            fetchImage(imageUUID, productDiv.querySelector(".product-image img"));
        }

        productGrid.appendChild(productDiv);
    });
}

function fetchImage(uuid, imageElement) {
    imageElement.style.width = "100%";
    imageElement.style.height = "100px";
    imageElement.style.objectFit = "cover";
    imageElement.style.borderRadius = "5px";

    fetch(`/api/image/show?uuid=${uuid}`)
        .then(response => response.blob())
        .then(blob => {
            imageElement.src = URL.createObjectURL(blob);
        })
        .catch(error => {
            console.error("Error loading image:", error);
            imageElement.src = "https://via.placeholder.com/75x75";
        });
}

function addToCartProduct(productId) {
    alert(`Product ID ${productId} added to cart!`);
}

document.addEventListener("DOMContentLoaded", () => {
    document.querySelector("#productGrid").addEventListener("click", function(event) {
        let targetElement = event.target;
        while (targetElement && !targetElement.classList.contains("store-view-btn")) {
            targetElement = targetElement.parentElement;
        }

        if (targetElement) {
            const productData = targetElement.getAttribute("data-product");
            try {
                viewProductDetails(JSON.parse(productData));
            } catch (error) {
                console.error("Error parsing product data:", error);
            }
        }
    });
});

function fetchItems() {
    clearTimeout(storeCategoryDebounceTimeout);
    const loadingSpinner = document.getElementById("loading-spinner");
    loadingSpinner.style.display = "block";

    const loadingSpinner2 = document.getElementById("loading-spinner_2");
    loadingSpinner2.style.display = "block";

    storeCategoryDebounceTimeout = setTimeout(() => {
        fetch('http://localhost:8080/internal/category/list')
                .then(response => response.json())
                .then(data => {
                    const container = document.getElementById("listContainer");
                    container.innerHTML = "";

                    if(data.length === 0) {
                        container.textContent = "No items available.";
                        return;
                    }
                    const button = document.createElement("button");
                    button.classList.add("item-button");
                    button.setAttribute("id", "clear-store-category");
                    button.style.backgroundColor = "#5dbea3";
                    button.style.textAlign = "center";
                    button.style.borderBottom = "1px dashed";
                    button.textContent = `𝑺𝒆𝒂𝒓𝒄𝒉 | ស្វែងរកតាមរយះ`;
                    button.onclick =function () {
                         fetchFilterProduct('/internal/product/list/filter?page=1&size=14');
                         clearStoreFilter = false;
                         resetFilterTitle();
                    };
                    container.appendChild(button);

                    data.forEach(item => {
                        const button = document.createElement("button");
                        button.classList.add("item-button");
                        button.textContent = `${item.name_kh} | ${item.name}`;
                        button.onclick = () => filterProductByCategoryId(`${item.id}`);
                        container.appendChild(button);
                    });
                })
                .catch(error => {
                    console.error("Error fetching data:", error);
                    document.getElementById("listContainer").textContent = "Failed to load data.";
                })
                .finally(() => {
                   loadingSpinner.style.display = "none";
                   loadingSpinner2.style.display = "none";
                });
    } ,500)
}

function formatStoreCurrency(amount, currency) {
    const formattedAmount = new Intl.NumberFormat('en-US', {
        minimumFractionDigits: currency === 'KHR' ? 0 : 2,
        maximumFractionDigits: currency === 'KHR' ? 0 : 2
    }).format(amount);

    const formattedRielAmount = new Intl.NumberFormat('en-US', {
        minimumFractionDigits: 0,
        maximumFractionDigits: 0
    }).format(amount * 4100);

    if (currency === 'USD') {
        return `${formattedAmount}$ = ${formattedRielAmount}៛`;
    } else if (currency === 'KHR') {
        const formattedUsdAmount = new Intl.NumberFormat('en-US', {
            minimumFractionDigits: 2,
            maximumFractionDigits: 2
        }).format(amount / 4100);
        return `${formattedUsdAmount}$ = ${formattedAmount}៛`;
    }
    return `${formattedAmount} ${currency}`;
}

function filterProductByCategoryId(id) {
    clearStoreFilter = true;
    resetFilterTitle();
    categoryGlobalId = id;
    fetchFilterProduct('http://localhost:8080/internal/product/list/filter?page=1&size=16&category_id=' + id + '&condition_type=EQUAL');
}

function lookupProductContains() {
    setTimeout(() => {
        let keyword = document.getElementById("store-keyword").value;
        let url = 'http://localhost:8080/internal/product/list/filter?page=1&size=14&condition_type=storeCondition&general=' + keyword;
        if(categoryGlobalId != 0) {
             url = url + '&category_id=' + categoryGlobalId;
        }
        fetchFilterProduct(url);
    }, 1500)
}

function displayDateTime() {
    const now = new Date();
    const formattedDate = now.toLocaleString("en-GB", {
      day: "2-digit",
      month: "2-digit",
      year: "numeric",
      hour: "2-digit",
      minute: "2-digit",
      second: "2-digit",
      hour12: false,
    });
    return formattedDate;
}

document.getElementById("store-keyword").addEventListener("input", lookupProductContains);
document.getElementById("clear-store-search").addEventListener("click", () => {
    location.reload()
;});

function closeProductDetails() {
    let container = document.querySelector('.store-view-detail-container');
    container.classList.remove('show');

    let productHeaderInfo = document.querySelector('.product-detail-information');
    productHeaderInfo.innerHTML = ''; // Clears content

    document.querySelector('.store-image-slider-container').innerHTML = '';
    document.querySelector('.store-image-thumbnail-container').innerHTML = '';

    document.getElementById("store-footer").style.display = "block";
}

function viewProductDetails(product) {
    let container = document.querySelector('.store-view-detail-container');
    container.classList.add('show');
    const imageSliderContainer = document.querySelector('.store-image-slider-container');
    const imageThumbnailContainer = document.querySelector('.store-image-thumbnail-container');
    document.getElementById("store-footer").style.display = "none";
    imageSliderContainer.innerHTML = '';
    imageThumbnailContainer.innerHTML = '';

    const imageUUIDs = product.variants[0]?.images?.map(image => image.uuid) || [];

    if (imageUUIDs.length === 0) {
        const noImageMessage = document.createElement('div');
        noImageMessage.textContent = 'No image preview available';
        noImageMessage.style.textAlign = 'center';
        noImageMessage.style.fontSize = '16px';
        noImageMessage.style.color = '#888'; // Optional styling
        imageSliderContainer.appendChild(noImageMessage);
        return;
    }

    imageUUIDs.forEach((uuid, index) => {
        const mainImage = document.createElement('img');
        mainImage.src = `http://localhost:8080/api/image/show?uuid=${uuid}`;
        mainImage.alt = `Image ${index + 1}`;
        mainImage.setAttribute('data-uuid', uuid);
        mainImage.style.display = (index === 0) ? 'block' : 'none';
        imageSliderContainer.appendChild(mainImage);
    });

    let currentThumbnailIndex = 0;
    function updateThumbnails() {
        imageThumbnailContainer.innerHTML = '';

        const imagesToShow = imageUUIDs.slice(currentThumbnailIndex, currentThumbnailIndex + 3); // Show 3 at a time
        imagesToShow.forEach((uuid, index) => {
            const thumbImage = document.createElement('img');
            thumbImage.src = `http://localhost:8080/api/image/show?uuid=${uuid}`;
            thumbImage.alt = `Thumb ${currentThumbnailIndex + index + 1}`;
            thumbImage.onclick = () => {
                goToSlide(currentThumbnailIndex + index);
            };
            imageThumbnailContainer.appendChild(thumbImage);
        });
    }

    updateThumbnails();

    let currentSlide = 0;
    function goToSlide(index) {
        const allImages = imageSliderContainer.querySelectorAll('img');
        allImages[currentSlide].style.display = 'none';
        currentSlide = index;
        allImages[currentSlide].style.display = 'block';
    }

    function shiftThumbnails(direction) {
        const totalImages = imageUUIDs.length;

        if (direction === "next") {
            if (currentThumbnailIndex + 3 < totalImages) {
                currentThumbnailIndex += 1;
            }
        } else if (direction === "prev") {
            if (currentThumbnailIndex > 0) {
                currentThumbnailIndex -= 1;
            }
        }

        updateThumbnails();
    }

    document.getElementById("go-next").addEventListener("click", () => {
        if (currentSlide < imageUUIDs.length - 1) {
            goToSlide(currentSlide + 1);
        } else {
            goToSlide(0);
        }

        shiftThumbnails("next");
    });

    document.getElementById("back-prev").addEventListener("click", () => {
        if (currentSlide > 0) {
            goToSlide(currentSlide - 1);
        } else {
            goToSlide(imageUUIDs.length - 1);
        }

        shiftThumbnails("prev");
    });

    let productHeaderInfo = document.querySelector('.product-detail-information');
    console.warn(product)
    if (productHeaderInfo) {
        productInfoDisplay(product,productHeaderInfo);
        createRightInfoContainer(product, productHeaderInfo);
        productQuantityDisplay(product, productHeaderInfo);

        if (product.description !== '') {
            let tabContainer = document.createElement("div");
            tabContainer.style.border = "1px solid #ccc";
            tabContainer.style.borderRadius = "5px";
            tabContainer.style.overflow = "hidden";
            tabContainer.style.width = "90%";
            tabContainer.style.marginTop = "8%";

            let tabButton = document.createElement("div");
            tabButton.textContent = "ពត៍មានបន្ថែម";
            tabButton.style.background = "#f4f4f4";
            tabButton.style.padding = "10px";
            tabButton.style.fontSize = "1.2rem";
            tabButton.style.cursor = "pointer";
            tabButton.style.borderBottom = "1px solid #ccc";

            let pTag = document.createElement("p");
            pTag.style.fontSize = "1.5rem";
            pTag.textContent = product.description;
            pTag.style.padding = "10px";
            pTag.style.display = "none"; // Initially hidden

            tabButton.addEventListener("click", function () {
                pTag.style.display = pTag.style.display === "none" ? "block" : "none";
            });

//            tabContainer.appendChild(tabButton); description
            tabContainer.appendChild(pTag);

            productHeaderInfo.appendChild(tabContainer);
        }
    }
}

function productInfoDisplay(product, productHeaderInfo) {
    let h1Element = document.createElement('h1');
    h1Element.style.fontSize = "2.5rem";
    h1Element.style.marginBottom = "-4%";
    h1Element.textContent = product.name_en + ' | ' + product.name_kh;
    productHeaderInfo.appendChild(h1Element);

    let productCodeEle = document.createElement('p');
    productCodeEle.style.fontSize = "2.5rem";
    productCodeEle.style.marginBottom = "-2%";
    productCodeEle.textContent = product.code;
    productHeaderInfo.appendChild(productCodeEle);

    let createDate = document.createElement("p");
    createDate.style.fontSize = "1.5rem";
    createDate.textContent = convertEpochToDateTime(product.created_at);
    productHeaderInfo.appendChild(createDate);
}

function productQuantityDisplay(product, productHeaderInfo) {
    if (product.sale_price) {
        let salePriceEle = document.createElement('h1');
        salePriceEle.style.fontSize = "2rem";
        salePriceEle.style.borderTop = "1px solid";
        salePriceEle.style.paddingTop = "2%";
        salePriceEle.textContent = formatCurrency(product.sale_price, product.currency);
        productHeaderInfo.appendChild(salePriceEle);
    }

    let quantityContainer = document.createElement("div");
    quantityContainer.style.display = "flex";
    quantityContainer.style.alignItems = "center";
    quantityContainer.style.gap = "10px";
    quantityContainer.style.marginTop = "20px";
    quantityContainer.style.border = "1px solid #ccc";
    quantityContainer.style.padding = "10px";
    quantityContainer.style.borderRadius = "5px";
    quantityContainer.style.width = "22%";
    quantityContainer.style.justifyContent = "center";

    let decreaseBtn = document.createElement("button");
    decreaseBtn.textContent = "−";
    decreaseBtn.style.fontSize = "1.5rem";
    decreaseBtn.style.width = "60px";
    decreaseBtn.style.cursor = "pointer";
    decreaseBtn.style.padding = "5px 10px";
    decreaseBtn.style.border = "none";
    decreaseBtn.style.background = "#ddd";
    decreaseBtn.style.borderRadius = "5px";

    let quantityDisplay = document.createElement("span");
    quantityDisplay.textContent = "1";
    quantityDisplay.style.fontSize = "1.5rem";
    quantityDisplay.style.minWidth = "30px";
    quantityDisplay.style.textAlign = "center";

    let increaseBtn = document.createElement("button");
    increaseBtn.textContent = "+";
    increaseBtn.style.fontSize = "1.5rem";
    increaseBtn.style.width = "65px";
    increaseBtn.style.cursor = "pointer";
    increaseBtn.style.padding = "5px 10px";
    increaseBtn.style.border = "none";
    increaseBtn.style.background = "#ddd";
    increaseBtn.style.borderRadius = "5px";

    let priceEstimate = document.createElement("p");
    priceEstimate.style.fontSize = "1.5rem";
    priceEstimate.style.marginTop = "10px";
    priceEstimate.textContent = `សរុប: ${formatCurrency(product.sale_price, product.currency)}`;

    let addToCartBtn = document.createElement("button");
    addToCartBtn.textContent = "Add to Cart";
    addToCartBtn.style.marginLeft = "10px";
    addToCartBtn.style.fontSize = "1.3rem";
    addToCartBtn.style.padding = "5px 10px";
    addToCartBtn.style.cursor = "pointer";
    addToCartBtn.style.border = "none";
    addToCartBtn.style.background = "#28a745";
    addToCartBtn.style.color = "white";
    addToCartBtn.style.borderRadius = "5px";
    addToCartBtn.style.width = "140px";
    addToCartBtn.style.height = "55px";

    let buyNowBtn = document.createElement("button");
    buyNowBtn.textContent = "Buy Now";
    buyNowBtn.style.marginLeft = "10px";
    buyNowBtn.style.fontSize = "1.3rem";
    buyNowBtn.style.padding = "5px 10px";
    buyNowBtn.style.cursor = "pointer";
    buyNowBtn.style.border = "none";
    buyNowBtn.style.background = "#ff9800";
    buyNowBtn.style.color = "white";
    buyNowBtn.style.borderRadius = "5px";
    buyNowBtn.style.width = "120px";
    buyNowBtn.style.height = "55px";

    let quantity = 1;

    function updatePrice() {
        let total = product.sale_price * quantity;
        priceEstimate.textContent = `សរុប: ${formatCurrency(total, product.currency)}`;
    }

    decreaseBtn.addEventListener("click", function () {
        if (quantity > 1) {
            quantity--;
            quantityDisplay.textContent = quantity;
            updatePrice();
        }
    });

    increaseBtn.addEventListener("click", function () {
        quantity++;
        quantityDisplay.textContent = quantity;
        updatePrice();
    });

    addToCartBtn.addEventListener("click", function () {
        alert(`Added ${quantity} item(s) to cart!`);
    });

    buyNowBtn.addEventListener("click", function () {
        alert(`Proceeding to checkout with ${quantity} item(s)!`);
    });

    quantityContainer.appendChild(decreaseBtn);
    quantityContainer.appendChild(quantityDisplay);
    quantityContainer.appendChild(increaseBtn);

    let footerContainer = document.createElement("div");
    footerContainer.style.width = "50%";
    footerContainer.style.position = "fixed";
    footerContainer.style.marginTop = "6%";

    let categoryInfo = document.createElement("p");
    categoryInfo.textContent = `ប្រភេទទំនិញ: ${product.category.name} | ${product.category.name_kh}`;
    categoryInfo.style.fontSize = "1.2rem";
    footerContainer.appendChild(categoryInfo);

    let stockQtyInfo = document.createElement("p");
    stockQtyInfo.textContent = 'ស្ដុក: ' + globalStockQuantities;
    stockQtyInfo.style.fontSize = "1.2rem";
    stockQtyInfo.style.marginTop = "-2%";
    footerContainer.appendChild(stockQtyInfo);

    productHeaderInfo.appendChild(quantityContainer);
    productHeaderInfo.appendChild(priceEstimate);
    productHeaderInfo.appendChild(addToCartBtn);
    productHeaderInfo.appendChild(buyNowBtn);
    productHeaderInfo.appendChild(footerContainer);
}
function createRightInfoContainer(product, parentContainer) {
    let rightContainer = document.createElement("div");
    rightContainer.style.width = "60%";
    rightContainer.style.display = "flex";
    rightContainer.style.flexDirection = "column";
    rightContainer.style.position = "absolute";
    rightContainer.style.top = "94%";
    rightContainer.style.right = "6%";
    rightContainer.style.overflow = "hidden";

    let searchInput = document.createElement("input");
    searchInput.type = "text";
    searchInput.placeholder = "ស្វែងរក...";
    searchInput.style.width = "100%";
    searchInput.style.padding = "8px";
    searchInput.style.marginBottom = "10px";
    searchInput.style.fontSize = "1.2rem";
    searchInput.style.border = "1px solid #ccc";
    searchInput.style.borderRadius = "5px";

    let variantInfoContainer = document.createElement("div");
    variantInfoContainer.style.maxHeight = "200px";
    variantInfoContainer.style.overflowY = "auto";
    variantInfoContainer.style.border = "1px solid #ccc";
    variantInfoContainer.style.padding = "10px";
    variantInfoContainer.style.borderRadius = "5px";
    variantInfoContainer.style.fontSize = "1.2rem";
    variantInfoContainer.style.whiteSpace = "normal";

    let groupedVariants = {};

    product.variants.forEach(variant => {
        globalStockQuantities = variant.stock_quantity;
        variant.attributes.forEach(attr => {
            let groupNum = attr.group_num;
            if (!groupedVariants[groupNum]) {
                groupedVariants[groupNum] = [];
            }
            groupedVariants[groupNum].push({
                variant,
                key: attr.name,
                value: attr.value
            });
        });
    });

    let sortedGroupNums = Object.keys(groupedVariants).sort((a, b) => a - b);
    sortedGroupNums.forEach(groupNum => {
        let groupHeader = document.createElement("div");
        groupHeader.style.borderBottom = "1px solid #ccc";
        groupHeader.style.paddingBottom = "3px";
        groupHeader.style.textAlign = "center";
        groupHeader.style.fontSize = "1.2rem";
        groupHeader.style.borderBottom = "1px solid";
        groupHeader.style.cursor = "pointer";
        groupHeader.style.display = "flex";
        groupHeader.style.alignItems = "center";

        let checkbox = document.createElement("input");
        checkbox.type = "checkbox";
        checkbox.style.marginRight = "10px";
        checkbox.style.width = "30px";
        checkbox.style.height = "30px";
        checkbox.style.cursor = "pointer";
        checkbox.id = `checkbox-group-${groupNum}`;
        checkbox.classList.add("variant-checkbox");
        checkbox.checked = false;

        groupHeader.appendChild(checkbox);

        let text = document.createElement("span");
        text.innerHTML = ` អង្គធាតុទី ${groupNum}`;
        groupHeader.appendChild(text);

        variantInfoContainer.appendChild(groupHeader);

        let keyContainer = document.createElement("div");
        keyContainer.style.display = "inline-block";
        keyContainer.style.width = "45%";
        keyContainer.style.borderRight = "1px solid";
        keyContainer.style.textAlign = "center";

        let valueContainer = document.createElement("div");
        valueContainer.style.display = "inline-block";
        valueContainer.style.width = "50%";
        valueContainer.style.paddingLeft = "3%";
        valueContainer.style.textAlign = "left";

        groupedVariants[groupNum].forEach(({ key, value }) => {
            let keyDiv = document.createElement("div");
            keyDiv.style.marginBottom = "5px";
            keyDiv.style.padding = "1px 0";
            keyDiv.style.fontSize = "1.2rem";
            keyDiv.innerText = key;

            let valueDiv = document.createElement("div");
            valueDiv.style.marginBottom = "5px";
            valueDiv.style.padding = "1px 0";
            valueDiv.style.fontSize = "1.2rem";
            valueDiv.innerText = value;

            keyContainer.appendChild(keyDiv);
            valueContainer.appendChild(valueDiv);
        });

        variantInfoContainer.appendChild(keyContainer);
        variantInfoContainer.appendChild(valueContainer);
    });

    variantInfoContainer.addEventListener("click", function(event) {
        if (event.target.tagName === "INPUT" && event.target.type === "checkbox") {
            const groupNum = event.target.id.split('-')[2];

            const groupData = {
                groupNum,
                variants: groupedVariants[groupNum]
            };

            if (event.target.checked) {
                selectedData.push(groupData);
            } else {
                selectedData = selectedData.filter(item => item.groupNum !== groupNum);
            }
            console.log("Selected Data:", selectedData);
        }
    });

searchInput.addEventListener("input", function () {
    let searchTerm = searchInput.value.toLowerCase(); // Get the search term and convert to lowercase
    variantInfoContainer.innerHTML = '';

    Object.keys(groupedVariants).forEach(groupNum => {
        let groupData = groupedVariants[groupNum];

        let groupHeader = document.createElement("div");
        groupHeader.style.borderBottom = "1px solid #ccc";
        groupHeader.style.paddingBottom = "3px";
        groupHeader.style.textAlign = "center";
        groupHeader.style.fontSize = "1.2rem";
        groupHeader.style.border = "1px solid";
        groupHeader.style.cursor = "pointer";
        groupHeader.style.display = "flex";
        groupHeader.style.alignItems = "center";

        let checkbox = document.createElement("input");
        checkbox.type = "checkbox";
        checkbox.style.marginRight = "10px";
        checkbox.style.width = "30px";
        checkbox.style.height = "30px";
        checkbox.style.cursor = "pointer";
        checkbox.id = `checkbox-group-${groupNum}`;
        checkbox.classList.add("variant-checkbox");
        checkbox.checked = false;

       let text = document.createElement("span");
        text.innerHTML = ` អង្គធាតុទី ${groupNum}`;
        groupHeader.appendChild(text);

        variantInfoContainer.appendChild(groupHeader);

        let keyContainer = document.createElement("div");
        keyContainer.style.display = "inline-block";
        keyContainer.style.width = "45%";
        keyContainer.style.borderRight = "1px solid";
        keyContainer.style.textAlign = "center";

        let valueContainer = document.createElement("div");
        valueContainer.style.display = "inline-block";
        valueContainer.style.width = "50%";
        valueContainer.style.paddingLeft = "3%";
        valueContainer.style.textAlign = "left";

        let foundVariants = false;

        groupData.forEach(({ key, value }) => {
            if (key.toLowerCase().includes(searchTerm) || value.toLowerCase().includes(searchTerm)) {
                let keyDiv = document.createElement("div");
                keyDiv.style.marginBottom = "5px";
                keyDiv.style.padding = "1px 0";
                keyDiv.style.fontSize = "1.2rem";

                let keyText = document.createElement("span");
                keyText.innerText = key;

                keyDiv.appendChild(keyText);

                let valueDiv = document.createElement("div");
                valueDiv.style.marginBottom = "5px";
                valueDiv.style.padding = "1px 0";
                valueDiv.style.fontSize = "1.2rem";

                let valueText = document.createElement("span");
                valueText.innerText = value;

                valueDiv.appendChild(valueText);

                keyContainer.appendChild(keyDiv);
                valueContainer.appendChild(valueDiv);

                foundVariants = true;
            }
        });
        if (foundVariants) {
            groupHeader.appendChild(checkbox);
            variantInfoContainer.appendChild(groupHeader);

            variantInfoContainer.appendChild(keyContainer);
            variantInfoContainer.appendChild(valueContainer);
        }
    });
});

    rightContainer.appendChild(searchInput);
    rightContainer.appendChild(variantInfoContainer);

    parentContainer.appendChild(rightContainer);
}

function convertEpochToDateTime(epoch) {
    let date = new Date(epoch);
    return date.toLocaleString();
}

function formatCurrency(amount, currency) {
    const formattedAmount = new Intl.NumberFormat('en-US', {
        minimumFractionDigits: currency === 'KHR' ? 0 : 2,
        maximumFractionDigits: currency === 'KHR' ? 0 : 2
    }).format(amount);

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
        }).format(amount / 4100);
        return `${formattedUsdAmount} $ = ${formattedAmount} ៛`;
    }
    return `${formattedAmount} ${currency}`;
}