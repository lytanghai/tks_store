let variant = {
    base_price: document.getElementById("product_base_price_currency_edit").value,
    currency: document.getElementById("product_base_price_edit").value,
    stock_quantity: document.getElementById("product_stock_quantity_edit").value,
    variant_attributes: [
    {
        attribute_id: 12,
        value: "test"
    }]
};

function addAttribute() {
    variant.variant_attributes.push({ attribute_id: "", value: "" });
    renderAttributes();
}

function updateVariant(key, value) {
    variant[key] = value;
}

function updateAttribute(attrIndex, key, value) {
    variant.variant_attributes[attrIndex][key] = value;
}


function renderAttributes() {
    const attrContainer = document.getElementById("product-variant-attribute-container");
    attrContainer.innerHTML = ""; // Clear the container before re-rendering

    selectedAttributes.clear(); // Reset selected attributes before rendering

    variant.variant_attributes.forEach((attr, attrIndex) => {
        const selectId = `attribute_select_${attrIndex}`; // Unique ID for each select

        if (attr.id) selectedAttributes.add(attr.id);

        const attrDiv = document.createElement("div");
        attrDiv.innerHTML = `
            <div style="margin-top: 2%; margin-left: 3%">
                <label style="font-size: 1.6rem;">
                    <select onchange="updateAttribute(${attrIndex}, 'អង្គធាតុទំនិញ', this.value)"
                        style="width: 200px; font-size: 1rem; height: 50px"
                        id="${selectId}">
                    </select>
                </label>
                <label style="font-size: 1.6rem">តម្លៃ:
                    <input type="text"
                        style="border:1px solid; width: 500px; font-size: 1.6rem;"
                        oninput="updateAttribute(${attrIndex}, 'value', this.value)">
                </label>
            </div>
        `;

        attrContainer.appendChild(attrDiv);
    });

    // Populate all dropdowns with stored attributes data after rendering
    variant.variant_attributes.forEach((attr, attrIndex) => {
        const selectId = `attribute_select_${attrIndex}`;
        populateAttributeDropdown(allAttributes, selectId, attr.id);
    });
}

function saveAttributeVariant() {
}
