let currentStoreProductPage = 0;
let totalStoreProductPage = 0;
let categoryGlobalId = 0;
let url = '';
const itemsPerPage = 14;

document.addEventListener("DOMContentLoaded", () => {
    url = `/internal/product/list/filter?page=${currentStoreProductPage}&size=${itemsPerPage}`;
    fetchFilterProduct(url);
    fetchItems();

    document.getElementById("prevPage").addEventListener("click", () => {
        if (currentStoreProductPage >= 1) {
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

    fetch(url)
        .then(response => response.json())
        .then(data => {
            console.log("API Response:", data);  // Log the response from the API
            if (!data.products || data.products.length === 0) {
                console.warn("No products found.");
                displayProducts([]);
                return;
            }

            totalStoreProductPage = data.totalPages - 1;
            document.getElementById("pageIndicator").textContent = `ទំព័រ ${currentStoreProductPage + 1} នៃ ${totalStoreProductPage}`;

            document.getElementById("prevPage").disabled = currentStoreProductPage === 1;
            document.getElementById("nextPage").disabled = currentStoreProductPage === totalStoreProductPage;

            displayProducts(data.products);
        })
        .catch(error => {
            console.error("Error fetching data:", error);
            displayProducts([]);
        });
}

function truncateText(text, maxLength) {
    return text.length > maxLength ? text.substring(0, maxLength) + "..." : text;
}

function displayProducts(products) {
    const productGrid = document.getElementById("productGrid");
    productGrid.innerHTML = "";

    if (products.length === 0) {
        productGrid.innerHTML = `<p style="color:#bbb;">No products available</p>`;
        return;
    }

    products.forEach(product => {
        const productDiv = document.createElement("div");
        productDiv.classList.add("grid-item");

        document.getElementById("store-search-size").textContent = products.length;
        document.getElementById("store-search-datetime").textContent = displayDateTime();

        const productNameEn = product.name_en || "";
        const productNameKh = product.name_kh || "";

        let productName = "";
        if(productNameKh === "") {
            productName = truncateText(productNameEn, 25);
        } else {
            productName = truncateText(productNameKh, 25);
        }

        const categoryNameEn = product.category.name || "";
        const categoryNameKh = product.category.name_kh || "";
        const categoryName = truncateText(categoryNameKh + " | " + categoryNameEn, 30);

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
                    <div class="product-category" title="${categoryNameEn}">${categoryNameEn} </br> ${categoryNameKh}</div>
                    <div class="product-price">${productPrice}</div>
                    <div class="product-stock">Stock:&nbsp;<span style="color: #e28743; font-weight: bold"> ${stockQuantity} </span></div>
                </div>
                <div class="product-actions">
                    <button class="action-btn view" onclick="viewProductDetails(${product.id})">👁️ View</button>
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

function viewProductDetails(productId) {
    alert(`Viewing details for Product ID: ${productId}`);
}

function fetchItems() {
    fetch('http://localhost:8080/internal/category/list') // Replace with your API endpoint
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
            button.style.backgroundColor = "#fafafa";
            button.style.textAlign = "center";
            button.style.borderBottom = "1px dashed";
            button.textContent = `ＲＥＦＲＥＳＨ`;
            button.onclick = () => location.reload();
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
        });
}

function formatStoreCurrency(amount, currency) {
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
    categoryGlobalId = id;
    fetchFilterProduct('http://localhost:8080/internal/product/list/filter?page=0&size=16&category_id=' + id + '&condition_type=EQUAL');
}

function lookupProductContains() {
    setTimeout(() => {
        let keyword = document.getElementById("store-keyword").value;
        let url = 'http://localhost:8080/internal/product/list/filter?page=0&size=16&condition_type=defaultCondition&general=' + keyword;
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