let currentStoreProductPage = 0;
let totalStoreProductPage = 0;
const itemsPerPage = 16;

document.addEventListener("DOMContentLoaded", () => {
    fetchFilterProduct();

    document.getElementById("prevPage").addEventListener("click", () => {
        if (currentStoreProductPage >= 1) {
            currentStoreProductPage--;
            fetchFilterProduct();
        }
    });

    document.getElementById("nextPage").addEventListener("click", () => {
        if (currentStoreProductPage < totalStoreProductPage) {
            currentStoreProductPage++;
            fetchFilterProduct();
        }
    });
});

function fetchFilterProduct() {
    const url = `/internal/product/list/filter?page=${currentStoreProductPage}&size=${itemsPerPage}`;

    console.log("Fetching URL:", url);  // Log the URL for debugging

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
            document.getElementById("pageIndicator").textContent = `Page ${currentStoreProductPage} of ${totalStoreProductPage}`;

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

        const productPrice = product.sale_price ? `${product.sale_price} ${product.currency || ""}` : "Price Unavailable";

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