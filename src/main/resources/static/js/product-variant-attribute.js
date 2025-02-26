let variant = {
    base_price: document.getElementById("product_base_price_currency_edit").value,
    currency: document.getElementById("product_base_price_edit").value,
    stock_quantity: document.getElementById("product_stock_quantity_edit").value,
    variant_attributes: [
        {
            attribute_id: 12,
            value: 'red'
        },
        {
            attribute_id: 13,
            value: 'M'
        }
    ]
};

function addAttribute() {
//    variant.variant_attributes.push({ attribute_id: "", value: "" });
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

    // 🟢 Store previous attribute selections and input values BEFORE clearing
    let tempValues = variant.variant_attributes.map((attr, index) => ({
        attribute_id: document.getElementById(`attribute_select_${index}`)?.value || attr.attribute_id || "",
        value: document.getElementById(`attribute_value_${index}`)?.value || attr.value || ""
    }));

    attrContainer.innerHTML = ""; // Clear container before re-rendering

    variant.variant_attributes.forEach((attr, attrIndex) => {
        const selectId = `attribute_select_${attrIndex}`;
        const inputId = `attribute_value_${attrIndex}`;

        const attrDiv = document.createElement("div");
        attrDiv.innerHTML = `
            <div style="margin-top: 2%; margin-left: 3%">
                <label style="font-size: 1.6rem;">
                    <select id="${selectId}"
                        style="width: 200px; font-size: 1rem; height: 50px"
                        onchange="updateAttribute(${attrIndex}, 'attribute_id', this.value)">
                            <option value="USD" selected disabled hidden style="font-size: 1.3rem">USD</option>
                    </select>
                </label>
                <label style="font-size: 1.6rem">តម្លៃ:
                    <input type="text" id="${inputId}"
                        style="border:1px solid; width: 500px; font-size: 1.6rem;"
                        oninput="updateAttribute(${attrIndex}, 'value', this.value)">
                </label>
            </div>
        `;

        attrContainer.appendChild(attrDiv);
        alert(document.getElementById("attribute_select_0").value)
    });

    // 🟢 Populate dropdowns and restore previous selections
    variant.variant_attributes.forEach((attr, attrIndex) => {
        const selectId = `attribute_select_${attrIndex}`;
        const inputId = `attribute_value_${attrIndex}`;

        populateAttributeDropdown(allAttributes, selectId, tempValues[attrIndex].attribute_id);

        document.getElementById(inputId).value = tempValues[attrIndex].value; // Restore input value
    });
}

//function populateAttributeDropdown(attributes, selectId, selectedId) {
//    const select = document.getElementById(selectId);
//    if (!select) return;
//
//    select.innerHTML = ""; // Clear previous options
//
//    const defaultOption = document.createElement("option");
//    defaultOption.value = "";
//    defaultOption.textContent = "Select an attribute";
//    select.appendChild(defaultOption);
//
//    attributes.forEach(attr => {
//        const option = document.createElement("option");
//        option.value = attr.id;
//        option.textContent = attr.name;
//
//        if (attr.id === selectedId) {
//            option.selected = true;
//        }
//
//        select.appendChild(option);
//    });
//}

function populateAttributeDropdown(attributes, selectId, selectedId) {
    const select = document.getElementById(selectId);
    if (!select) return;

    select.innerHTML = ""; // Clear previous options

    const defaultOption = document.createElement("option");
    defaultOption.value = "";
    defaultOption.textContent = "Select an attribute";
    select.appendChild(defaultOption);

    attributes.forEach(attr => {
        const option = document.createElement("option");
        option.value = attr.id;
        option.textContent = `${attr.id} - ${attr.name}`; // Display both attribute_id and name

        if (attr.id === selectedId) {
            option.selected = true;
        }

        select.appendChild(option);
    });
}

function fetchAttributes() {
    fetch("/internal/attribute/list")
        .then(response => response.json())
        .then(attributes => {
            allAttributes = attributes;
        })
        .catch(error => {
            console.error("Error fetching attributes:", error);
        });
}

document.addEventListener("DOMContentLoaded", fetchAttributes);
