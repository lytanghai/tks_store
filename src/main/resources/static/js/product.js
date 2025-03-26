if(window.location.pathname.includes("/api/product")) {
    function openCreateUpdateProductModal(element) {
            let id = '';
            let nameEn = '';
            let nameKh = '';
            let code = '';
            let categoryNameEn = '';
            let categoryNameKh = '';
            let currency = '';
            let salePrice = '';
            let description = '';
            let basePrice = '';
            let basePriceCurrency = '';
            let stockQuantity = '';
            let sku = '';
            let images = [];
            let attributes = [];

        var iconElement = document.querySelector(".icon-service-type");
        var formTitle = element.getAttribute("data-form-title");

        showTab("Product");

        if(formTitle === 'Create') {
            document.getElementById("form-modal-product-title").textContent = 'បន្ទាប់';
            document.getElementById("form-product-create-title").textContent = 'បញ្ញូលពត៍មានទំនិញ';
            document.getElementById("form-modal-product-title-2").textContent = 'បញ្ជូលអង្គធាតុទំនិញ';

            iconElement.src = "/icon/new-product-icon.png";
            iconElement.alt = "new-product-icon.png";
            document.getElementById("product-category-edit").style.display = "none";
            document.getElementById("product_currency_edit").value = 'USD'
            document.getElementById("product-submit-btn").value = "create";
            document.getElementById("product-submit-btn").textContent = "បន្ថែមទំនិញ";

        } else if(formTitle == 'Update') {
            id = element.getAttribute("id");
            nameEn = element.getAttribute("data-name-en");
            nameKh = element.getAttribute("data-name-kh");
            code = element.getAttribute("data-code");
            categoryNameEn = element.getAttribute("data-categoryEn");
            categoryNameKh = element.getAttribute("data-categoryKh");
            currency = element.getAttribute("data-currency");
            salePrice = element.getAttribute("data-sale-price");
            description = element.getAttribute("data-description");

            variantId = element.getAttribute("data-variantId");
            basePrice = element.getAttribute("data-base-price");
            basePriceCurrency = element.getAttribute("data-base-price-currency");
            stockQuantity = element.getAttribute("data-stock-quantity");
            sku = element.getAttribute("data-sku");

            showExistingImage(element.getAttribute("data-images"));

            const rawAttributeString = element.getAttribute("data-attributes");

            if(rawAttributeString !== null) {
                const attributeArr = convertToJSONArray(rawAttributeString);
                attributes = JSON.stringify(attributeArr, null, 4)
            }

            document.getElementById("product_base_price_edit").value = basePrice;
            document.getElementById("product_base_price_currency_edit").value = basePriceCurrency;
            document.getElementById("product_stock_quantity_edit").value = stockQuantity;
            document.getElementById("product_stock_sku").value = sku;

            variantAttributes = attributes;

            document.getElementById("form-modal-product-title").textContent = 'បន្ទាប់';
            document.getElementById("form-product-create-title").textContent = 'កែប្រែទិន្ន័យផលិតផលចាស់';
            document.getElementById("form-modal-product-title-2").textContent = 'កែប្រែអង្គធាតុទំនិញ';
            document.getElementById("product-submit-btn").textContent = "កែប្រែទំនិញ";
            iconElement.src = "/icon/edit-product-icon.png";
            iconElement.alt = "edit-product-icon.png";
            document.getElementById("product-category-edit").style.display = "block";
            document.getElementById("product-category-edit").value = categoryNameEn + ' / ' + categoryNameKh;
            document.getElementById("product-submit-btn").value = "update";
        }

        document.getElementById("product_id_edit").value = id;
        document.getElementById("product_name_en_edit").value = nameEn;
        document.getElementById("product_name_kh_edit").value = nameKh;
        document.getElementById("product_code_edit").value = code;
        document.getElementById("product_description_edit").value = description;
        document.getElementById("product_currency_edit").value = currency;
        document.getElementById("product_sale_price_edit").value = salePrice;
        document.getElementById("myProductModal").style.display = "block";
    }

    function addToRemoveImg() {
        console.log("removed")
        if (imageUrls.length === 0) {
            return;
        }
        currentImageIndex = 0;
        let imageUrl = imageUrls[currentImageIndex];
        let uuid = imageUrl.split("/").pop();
        console.log('uuid' + uuid)

        if(imageUrls.length === 1) {
            imageUrls = [];
            document.getElementById("image_preview_modal").style.display = "none";
            document.getElementById("preview_button").style.display = "none";
            return;
        }

        currentImageIndex = currentImageIndex + 1;
        imageIds.push(uuid);
        imageUrls.splice(currentImageIndex , 1)

        currentIndex = (currentIndex + 1) % imageUrls.length;
        document.getElementById('preview_image').src = imageUrls[currentIndex];
    }
    function showExistingImage(imageData) {
        let baseUrl = "/api/image/show?uuid=";

        let entries = imageData.split(",");

        entries.forEach(entry => {
            let uuid = entry.split(":")[1];
            let imageUrl = `${baseUrl}${uuid}`;
            imageUrls.push(imageUrl);
        });
    }

    function showProductVerify() {

    //Product
        document.getElementById("verify-product-id").innerHTML = document.getElementById("product_id_edit").value
        document.getElementById("verify-product-nameEn").innerHTML = document.getElementById("product_name_en_edit").value
        document.getElementById("verify-product-nameKh").innerHTML = document.getElementById("product_name_kh_edit").value
        document.getElementById("verify-product-code").innerHTML = document.getElementById("product_code_edit").value
        if(document.getElementById("product-category-edit").value !== '') {
            document.getElementById("verify-product-category").innerHTML = document.getElementById("product-category-edit").value;
        } else {
            let selectElement = document.getElementById("product_select");
            let selectedValue = selectElement.value;

            document.getElementById("verify-product-category").innerHTML = getSelectOptionTextByValue(selectElement, selectedValue);
        }
        document.getElementById("verify-product-sale-price").innerHTML = document.getElementById("product_sale_price_edit").value
        document.getElementById("verify-product-currency").innerHTML = document.getElementById("product_currency_edit").value
        document.getElementById("verify-product-description").innerHTML = document.getElementById("product_description_edit").value

    //    Variant
        document.getElementById("verify-variant-base-price").innerHTML = parseFloat(document.getElementById('product_base_price_edit').value) | '';
        document.getElementById("verify-variant-currency").innerHTML = document.getElementById('product_base_price_currency_edit').value | '';
        document.getElementById("verify-variant-stock-quantity").innerHTML = parseInt(document.getElementById('product_stock_quantity_edit').value) | '';
        document.getElementById("verify-variant-sku-code").innerHTML = document.getElementById('product_stock_sku').value

        getLiElementsContentAsArray();
        document.getElementById("verify-variant-attribute-name").innerHTML = resultList.join('</br>');

    //    Image
        showVerifyImageSlider();
    }

    function addAttribute() {
        let selectElement = document.getElementById("product_attribute_select");
        let attributeId = parseInt(selectElement.value);
        let attributeName = selectElement.options[selectElement.selectedIndex].text;
        let value = document.getElementById("product_attribute_value").value.trim();
        if (!value) {
            alert("Please enter a value for the attribute.");
            return;
        }

        let existingIndex = '';
        if(typeof(variantAttributes) === 'object') {
            //create
            existingIndex = variantAttributes.findIndex(attr => attr.attribute_id === attributeId);
//            if (existingIndex !== -1) {
//                alert("This attribute is already added!");
//                return;
//            }
             variantAttributes.push({
                group_num: groupNum,
                attribute_id: attributeId,
                name: attributeName,
                value: value
            });
            updateAttributeList();

        } else {
            variantAttributes = JSON.parse(variantAttributes);
            existingIndex = variantAttributes.findIndex(attr => attr.name === attributeName);

//            if (existingIndex !== -1) {
//                alert("This attribute is already added!");
//                return;
//            }
            variantAttributes.push({
                group_num: groupNum,
                attribute_id: attributeId,
                name: attributeName,
                value: value
            });
            variantAttributes = JSON.stringify(variantAttributes);
            displayVariantAttributes(variantAttributes);
            document.getElementById("product_attribute_value").value = '';
            document.getElementById("product_attribute_select").value = '';
        }

    }

    function updateAttributeList() {
        let selectElement = document.getElementById("product_attribute_select");
        let attributeId = selectElement.value; // Get selected attribute ID
        let attributeName = selectElement.options[selectElement.selectedIndex].text; // Get selected attribute name
        let value = document.getElementById("product_attribute_value").value;

        if (!value) {
            alert("Please enter a value for the attribute.");
            return;
        }

        let listItem = document.createElement("li");
        listItem.style.display = "flex";
        listItem.style.textAlign = "center";
        listItem.style.fontSize = "1.6rem";
        listItem.style.paddingLeft = "5%";
        listItem.style.backgroundColor = "#fff";
        listItem.setAttribute("data-id", attributeId);

        let attrSpan = document.createElement("span");
        attrSpan.classList.add("attribute-name");
        attrSpan.style.flex = "1";
        attrSpan.style.marginLeft = '2%';
        attrSpan.textContent = attributeName;

        let valueSpan = document.createElement("span");
        valueSpan.classList.add("attribute-value");
        valueSpan.style.flex = "1";
        valueSpan.style.paddingRight = "3%";
        valueSpan.textContent = value;

        let deleteButton = document.createElement("button");
        deleteButton.textContent = "Delete";
        deleteButton.style.flex = "1";
        deleteButton.style.backgroundColor = "#fff";
        deleteButton.style.width = "100px";
        deleteButton.innerHTML = '<img src="/icon/trash.png" class="icon" alt="Trash Icon">';
        deleteButton.addEventListener("click", function() {
            removeAttributeItem(listItem);
        });

        listItem.appendChild(attrSpan);
        listItem.appendChild(valueSpan);
        listItem.appendChild(deleteButton);

        document.getElementById("attribute-list").appendChild(listItem);

        document.getElementById("product_attribute_select").value = "";
        document.getElementById("product_attribute_value").value = "";
    }

    function removeAttributeItem(listItem) {
        listItem.parentNode.removeChild(listItem);
        let selectElement = document.getElementById("product_attribute_select");
        let attributeId = parseInt(selectElement.value);
        let existingIndex = variantAttributes.findIndex(attr => attr.attribute_id === attributeId);
        if (existingIndex !== -1) {
            variantAttributes.splice(existingIndex, 1);
        }
    }

    function toggleInputFields() {
        const condition = document.getElementById("filterCondition").value;
        const inFields = document.getElementById("inConditionFields");
        const dateFilter = document.getElementById("dateFilter");
        const singleField = document.getElementById("singleConditionField");

        if (condition === "BETWEEN") {
            inFields.style.display = "block";
            singleField.style.display = "none";
            dateFilter.style.display = "none";
        } else if (condition === "EQUAL" || condition === "GREATER_THAN" || condition === "LESS_THAN") {
            inFields.style.display = "none";
            singleField.style.display = "block";
            dateFilter.style.display = "none";
        } else if (condition === "9") {
            inFields.style.display = "none";
            singleField.style.display = "none";
            dateFilter.style.display = "block";
        } else {
            inFields.style.display = "none";
            singleField.style.display = "block";
            dateFilter.style.display = "none";
        }
    }

    //const selectElement = document.getElementById('filterColumn');
    //const condition = document.getElementById('filterCondition');

    function clearProductStartDate() {
        let startDateInput = document.getElementById("productStartDate");
        startDateInput.value = "";
        startDateInput.dispatchEvent(new Event("change"));
    }

    function clearProductEndDate() {
        let endDateInput = document.getElementById("productEndDate");
        endDateInput.value = "";
        endDateInput.dispatchEvent(new Event("change"));
    }

    function populateCategoryDropdown(categories) {
        const categorySelect = document.getElementById("product_select");
        categorySelect.innerHTML = "";

        let generalCategory;

        const optgroup = document.createElement("optgroup");

        categories.forEach(category => {
            const option = document.createElement("option");
            option.value = category.id;
    //        option.text = `${category.name} / ${category.name_kh}`;
            option.text = `${category.name_kh} / ${category.name}`;
            option.style.fontSize = "1.6rem";
            option.style.border= "1px solid #0a0a0a";

            if (category.name === "ទូទៅ") {
                generalCategory = option;
            } else {
                optgroup.appendChild(option);
            }
        });

        categorySelect.appendChild(optgroup);
    }

    function populateAttributeDropdown(attributes) {
        const attributeSelect = document.getElementById("product_attribute_select");
        attributeSelect.innerHTML = "";
        const groups = {};

        attributes.forEach(attr => {
            if (!groups[attr.category]) {
                const optgroup = document.createElement("optgroup");
                groups[attr.category] = optgroup;
                attributeSelect.appendChild(optgroup);
            }

            const option = document.createElement("option");
            option.style.width= "auto";

            option.value = attr.id;
            option.text = attr.name_kh && attr.name_kh.trim() !== ""
                ? `${attr.name} (${attr.name_kh})`
                : attr.name;

            groups[attr.category].appendChild(option);
        });
    }

    function showConfirmationModal(action, id) {
        document.getElementById("product-confirmation-modal").style.display = "block";
        const modalText = document.getElementById("product-confirm-modal-text");
        if (action === 'create') {
            modalText.textContent = 'តើអ្នកប្រាកដថាចង់បញ្ជូលផលិតផលថ្មីមែនទេ?';
        } else if (action === 'update') {
            modalText.textContent = 'តើអ្នកប្រាកដថាចង់ធ្វើការកែប្រែផលិតផលនេះទេ?';
        } else if (action === 'delete') {
            modalText.textContent = 'តើអ្នកប្រាកដថាចង់ធ្វើការលុបផលិតផលនេះទេ?';
        }
        window.currentAction = action;
        window.productId = id;
    }

    function closeConfirmationModal() {
        document.getElementById("product-confirmation-modal").style.display = "none";
    }

    function confirmProductActionConfirmation() {
        if (window.currentAction === 'create') {
            uploadProduct();
            closeModal();
        } else if (window.currentAction === 'update') {
    //        updateProduct();
            updateProductDetail();
            closeModal();
        } else if (window.currentAction === 'delete') {
            deleteProduct(window.productId);
        }
        updateServiceIcon();
        closeConfirmationModal();
    }

    function handleFormSubmit(event) {
        event.preventDefault();
        const titleText = document.getElementById("form-modal-product-title").textContent.toLowerCase();
        if(titleText === 'បន្ទាប់') {

        }
        else if(titleText.includes("បញ្ជូល")) {
            showConfirmationModal('create');
        } else {
            showConfirmationModal('update');
        }
    }

    function updateServiceIcon() {
        const titleElement = document.getElementById("form-modal-product-title");
        const iconElement = document.querySelector(".icon-service-type");
        const titleText = titleElement.textContent.trim().toLowerCase();

        if (titleText === "create") {
            iconElement.src = "/icon/new-product-icon.png";
            iconElement.alt = "new-product-icon.png";
        } else if (titleText === "update") {
            iconElement.src = "/icon/edit-product-icon.png";
            iconElement.alt = "edit-product-icon.png";
        } else {
            iconElement.src = "/icon/category-icon.png";
            iconElement.alt = "category-icon.png";
        }
    }

    function checkButtonAction() {
        const button = document.getElementById("product-submit-btn").value;
        if (button === "create") {
            showConfirmationModal('create');
        } else if (button === "update") {
            showConfirmationModal('update');
        } else {
            console.error("Unknown action for product-submit-btn");
        }
    }

    function closeModal() {
        document.getElementById("myProductModal").style.display = "none";
        document.getElementById("product-category-edit").value = "";
        document.getElementById("product_base_price_edit").value = "";
        document.getElementById("product_base_price_currency_edit").value = "";
        document.getElementById("product_stock_quantity_edit").value = "";
        document.getElementById("product_stock_sku").value = "";
        document.getElementById("product_attribute_select").value = "";
        document.getElementById("product_attribute_value").value = "";
        document.getElementById("product_variant_image_value").value = "";
        document.getElementById("verify-variant-attribute-name").innerHTML = ''
        document.getElementById("attribute-list").textContent = ''
        resultList = [];
        variantAttributes = [];
    //    reloadPage();
    }

    function showTab(tabName) {
        let tabcontent = document.getElementsByClassName("tabcontent");
        let tablinks = document.getElementsByClassName("tablinks");

        for (let i = 0; i < tabcontent.length; i++) {
            tabcontent[i].style.display = "none";
        }

        for (let i = 0; i < tablinks.length; i++) {
            tablinks[i].classList.remove("active");
        }
        document.getElementById(tabName).style.display = "block";
        document.getElementById("tab-" + tabName).classList.add("active");
        currentTab = tabName;

        if(currentTab === 'Verify') {
            showProductVerify();
            truncateTextIfLongerThan200();
        }
        event.preventDefault();
    }

    function navigateTab(current, next) {
        showTab(next);
    }

    function truncateTextIfLongerThan200() {
        const labelElement = document.getElementById("verify-product-description");
        const text = labelElement.textContent;

        if (text.length > 200) {
            labelElement.textContent = text.slice(0, 200) + ".....";
        }
    }

    function getLiElementsContentAsArray() {
        let ulElement = document.getElementById('attribute-list');
        let liElements = ulElement.querySelectorAll('li');
        if(liElements.length === 0) {
            resultList = [];
            return;
        }
        if(document.getElementById("attribute-list").textContent !== '') {
            liElements.forEach((liElement, index) => {
                let attributeName = liElement.querySelector('.attribute-name').textContent;
                let attributeValue = liElement.querySelector('.attribute-value').textContent;
                let content = `អង្គធាតុទី ${index + 1}: ${attributeName} - តម្លៃអង្គធាតុ ${attributeValue} <br>`;
                if (!resultList.includes(content)) {
                    resultList.push(content);
                }
            });
        }
    }

    function nextProductImage() {

        if (currentImageIndex < imageUUIDs.length - 1) {
            currentImageIndex++; // Move to the next image
        } else {
            currentImageIndex = 0; // Reset to the first image
        }
        showImage();
    }

    function prevProductImage() {
        console.log("currenct Image Index: " + currentImageIndex)
        if (currentImageIndex > 0) {
            currentImageIndex--;
        } else {
            currentImageIndex++;
        }
        showImage();
    }

    function closeSlider() {
        document.getElementById("imageSlider").style.display = "none";
    }

    function closeVariantAttributeModal() {
    //    document.getElementById("customModal").style.display = "none";
    }

    function displayVariantAttributes(attributes) {
        const attributeListContainer = document.getElementById('attribute-list');
        attributeListContainer.innerHTML = "";

        try {
            if(variantAttributes.length > 0) {
                const attributes = JSON.parse(variantAttributes);
                console.log('attri ' + JSON.stringify(attributes))
                if (Array.isArray(attributes)) {
                    attributes.forEach((attribute, index) => {
                    const listItem = document.createElement('li');
                    listItem.style.display = 'flex';
                    listItem.style.textAlign = "center";
                    listItem.style.fontSize = "1.6rem";
                    listItem.style.paddingRight = "2%";
                    listItem.style.backgroundColor = "#fff";

                    const idSpan = document.createElement('span');
                    idSpan.classList.add('attribute-id');
                    idSpan.style.flex = "1";
                    idSpan.textContent = attribute.id;

                    const nameSpan = document.createElement('span');
                    nameSpan.classList.add('attribute-name');
                    nameSpan.style.flex = "1";
                    nameSpan.textContent = attribute.name + '[' + attribute.group_num + ']';

                    const valueSpan = document.createElement('span');
                    valueSpan.classList.add('attribute-value');
                    valueSpan.style.flex = "1";
                    valueSpan.style.paddingRight = "10%";
                    valueSpan.style.marginRight = "6%";
                    valueSpan.textContent = attribute.value;

                    const actionsSpan = document.createElement('span');
                    actionsSpan.classList.add('product-attribute-actions');
                    actionsSpan.style.marginRight = "10%";

                    const deleteButton = document.createElement('button');
                    deleteButton.textContent = 'Delete';
                    deleteButton.style.flex = "1";
                    deleteButton.style.backgroundColor = "#fff";
                    deleteButton.style.paddingLeft = "0%";
                    deleteButton.style.zIndex = "-4";
                    deleteButton.innerHTML = '<img src="/icon/trash.png" class="icon" alt="Trash Icon">';
                    deleteButton.onclick = () => removeAttributeUpdateItem(variantAttributes, index);

                    actionsSpan.appendChild(deleteButton);

                    listItem.appendChild(nameSpan);
                    listItem.appendChild(valueSpan);
                    listItem.appendChild(actionsSpan);

                    attributeListContainer.appendChild(listItem);
                });
                } else {
                    console.error('Expected an array for attributes, but got:', attributes);
                }
            }

        } catch (e) {
            console.error('Failed to parse attributes:', e);
        }
    }


    function removeAttributeUpdateItem(attributes, index) {
       attributes = JSON.parse(attributes);
       if (index > -1 && index < attributes.length) {
           attributes.splice(index, 1);
       }
       variantAttributes = JSON.stringify(attributes);
       displayVariantAttributes(variantAttributes);
    }

    const checkVariantButtonUpdate = document.getElementById("product-next-btn");
    checkVariantButtonUpdate.addEventListener("click", function() {
        if(variantAttributes !== '') {
            displayVariantAttributes(variantAttributes);
        }
        if(imageUrls !== '') {
            checkAndShowPreviewEditButton(imageUrls);
        }
    })

    function checkAndShowPreviewEditButton(imageUrls) {
        const previewButton = document.getElementById('preview_button');
        imageUUIDs = imageUrls.map(img => img.value);

        if (imageUUIDs.length > 0) {
            previewButton.style.display = "block";
            previewButton.onclick = () => openPreview(0);
        } else {
            previewButton.style.display = "none";
        }
    }

    function addNewGroupVariantAttribute() {
        groupNum += 1;
        document.getElementById("attribute-list").innerHTML = "";
    }

}
