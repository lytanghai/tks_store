let currentStoreProductPage = 0;
let totalStoreProductPage = 0;

document.addEventListener("DOMContentLoaded", () => {
    fetchFilterProduct();

    document.getElementById("prevPage").addEventListener("click", () => {
        if (currentStoreProductPage > 1) {
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
    const url = `/internal/product/list/filter?page=${currentStoreProductPage}`;

    fetch(url)
        .then(response => response.json())
        .then(data => {
            console.log("API Response:", data); // Debugging step

            if (!data.products || data.products.length === 0) {
                console.warn("No products found.");
                displayProducts([]);
                return;
            }

            totalStoreProductPage = data.totalPages;
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

function displayProducts(products) {
    const productGrid = document.getElementById("productGrid");
    productGrid.innerHTML = "";
    const totalCells = 25;

    for (let i = 0; i < totalCells; i++) {
        const productDiv = document.createElement("div");
        productDiv.classList.add("grid-item");

        if (products[i]) {
            const product = products[i];
            const productName = product.name_en || "Unnamed Product";
            const productPrice = product.sale_price ? `${product.sale_price} ${product.currency || ""}` : "Price Unavailable";

            // Handle image URL from variants
            const variant = product.variants[0]; // We'll just use the first variant for simplicity
            const imageUUID = variant.images?.[0]?.uuid;

            if (imageUUID) {
                // Fetch the image using the uuid and display it
                fetchImage(imageUUID, productDiv);
            } else {
                // Fallback to placeholder if no image exists
                productDiv.innerHTML = `
                    <img src="https://via.placeholder.com/100" alt="${productName}">
                    <h4>${productName}</h4>
                    <p>${productPrice}</p>
                    <button onclick="editProduct(${product.id})">Edit</button>
                `;
            }
        } else {
            productDiv.classList.add("grid-placeholder");
            productDiv.innerHTML = `<p style="color:#bbb;">Empty Slot</p>`;
        }

        productGrid.appendChild(productDiv);
    }
}

function fetchImage(uuid, productDiv) {
    const imageElement = document.createElement("img");
    fetch(`/api/image/show?uuid=${uuid}`)
        .then(response => response.blob())
        .then(blob => {
            imageElement.src = URL.createObjectURL(blob);
            productDiv.appendChild(imageElement);
        })
        .catch(error => {
            console.error("Error loading image:", error);
            imageElement.src = "https://via.placeholder.com/100"; // Fallback image
            productDiv.appendChild(imageElement);
        });
}

function editProduct(productId) {
    alert(`Editing product ID: ${productId}`);
}
