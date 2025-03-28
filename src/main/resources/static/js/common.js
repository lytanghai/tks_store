window.onload = function() {
    clearAttributeModalInput();
    clearCategoryModalInput();

    if(window.location.pathname.includes("/api/product")) {
        showTab("Product");
        fetchCategories();
        fetchAttributes();
    }

    const popupType = sessionStorage.getItem('popupMessage');
    const popupAction = sessionStorage.getItem('popupAction');
    if (popupType) {
        sessionStorage.removeItem('popupMessage');
        sessionStorage.removeItem('popupAction');

        setTimeout(() => {
            const popup = document.getElementById('pop_up_message');
            popup.className = `pop-up-message ${popupType}`;

            if(popupAction === 'update') {
               popup.textContent = popupType === 'success' ? '✔ ទិន្ន័យត្រូវបាន'+ 'កែប្រែ' +'បានដោយជោគជ័យ!' : '❌ ទិន្ន័យមិនអាច'+ 'កែប្រែ' +'បានទេ!';
            } else if(popupAction === 'create') {
               popup.textContent = popupType === 'success' ? '✔ ទិន្ន័យត្រូវបាន'+ 'បង្កើត' +'បានដោយជោគជ័យ!' : '❌ ទិន្ន័យមិនអាច'+ 'បង្កើត' +'បានទេ!';
            } else if(popupAction === 'delete') {
                popup.textContent = popupType === 'success' ? '✔ ទិន្ន័យត្រូវបាន'+ 'លុប' +'បានដោយជោគជ័យ!' : '❌ ទិន្ន័យមិនអាច'+ 'លុប' +'បានទេ!';
             }

            popup.style.display = 'block';
            setTimeout(() => popup.style.opacity = '1', 500);

            setTimeout(() => {
                popup.style.opacity = '0';
                setTimeout(() => popup.style.display = 'none', 1000);
            }, 2000);
        }, 500);
    }
};

document.addEventListener("DOMContentLoaded", function () {
    if (path === "/api/product/list/filter" && query === "?page=1") {
        setTimeout(() => {
            fetchFilterProduct();
        }, 500);
    }
});
document.addEventListener("click", function(event) {
      let modal = document.getElementById("myProductModal");
      if (event.target === modal) {
          event.stopPropagation(); // Prevent closing the modal
      }

    //close preview image on product list when click anywhere
//    document.getElementById("imageSlider").style.display = 'none';
    document.getElementById("customModal").style.display = 'none';

  });

document.addEventListener('keydown', function (event) {
  if (event.ctrlKey && (event.key === '+' || event.key === '-' || event.key === '0')) {
    event.preventDefault();
  }
});

document.addEventListener('wheel', function (event) {
  if (event.ctrlKey) {
    event.preventDefault();
  }
}, { passive: false });

function showPopUpMessage(type, action) {
    sessionStorage.setItem('popupMessage', type);
    sessionStorage.setItem('popupAction', action);
    location.reload();
}

function reloadPage() {
    location.reload();
}

function getSelectOptionTextByValue(selectElement, selectedValue) {
    for (let i = 0; i < selectElement.options.length; i++) {
        if (selectElement.options[i].value === selectedValue) {
            return selectElement.options[i].text;
        }
    }
    return null;
}

function addImages(event) {
    const files = event.target.files;

    for (let i = 0; i < files.length; i++) {
        if (files[i].type.startsWith('image/')) {
            imageUrls.push(URL.createObjectURL(files[i])); // Append new images
        } else {
            alert('សូមបញ្ជូលឯកសាររូបភាពប៉ុណ្ណោះ!');
            return;
        }
    }

    if (imageUrls.length > 0) {
        document.getElementById('preview_button').style.display = 'inline-block';
    }
}

function openPreview() {
    if (imageUrls.length > 0) {
        document.getElementById('preview_image').src = imageUrls[currentIndex];
        document.getElementById('image_preview_modal').style.display = 'block';
    }
    showImageSlider();
}

function closePreview() {
    document.getElementById('image_preview_modal').style.display = 'none';
}

function prevImage(id) {
    if (imageUrls.length > 0) {
        currentIndex = (currentIndex - 1 + imageUrls.length) % imageUrls.length;
        document.getElementById(id).src = imageUrls[currentIndex];
    }
}

function nextImage(id) {
    if (imageUrls.length > 0) {
        currentIndex = (currentIndex + 1) % imageUrls.length;
        document.getElementById(id).src = imageUrls[currentIndex];
    }
}

function prevImageUpload(event) {
       event.preventDefault();
       event.stopPropagation();
    if (imageUrls.length > 0) {
        currentIndex = (currentIndex - 1 + imageUrls.length) % imageUrls.length;
        document.getElementById('preview_image').src = imageUrls[currentIndex];
    }
}

function nextImageUpload(event) {
       event.preventDefault();
       event.stopPropagation();
    if (imageUrls.length > 0) {
        currentIndex = (currentIndex + 1) % imageUrls.length;
        document.getElementById('preview_image').src = imageUrls[currentIndex];
    }
}

function showImageSlider() {
    const modal = document.getElementById('preview_image');
    if (event.target == modal) {
        document.getElementById("image_preview_modal").style.display = 'none';
    }
}


function showVerifyImageSlider() {
    if (imageUrls.length > 0) {
        document.getElementById('verify_preview_image').src = imageUrls[currentIndex];
        document.getElementById('image_verify_preview_modal').style.display = 'block';
    } else {
        document.getElementById('verify_preview_image').style.display = "none";
        document.getElementById('image_verify_preview_modal').style.display = 'none';
    }
}

function extractNumber(value) {
    // Extract numeric part from value (e.g., "100 USD" → 100)
    let number = parseFloat(value.replace(/[^\d.]/g, ""));
    return isNaN(number) ? null : number;
}

function convertToJSONArray(input) {
    console.log('convertToJSONArray' + input)
        console.log('4 group_num: ')
    return input.split(',').map(pair => {
        let [id, name, value, group_num] = pair.split(':').map(item => item.trim());
        return { id , name, value, group_num };
    });
}

function showAlertMessageModal(messageTitle, messageBody) {
    const modal = document.getElementById("alert_msg_modal");
    const modalMessage = document.getElementById("alert_modal_message_title");
    const modalMessageBody = document.getElementById("alert_modal_message_body");
    const modalImg = document.getElementById("alert-msg-image");
    modalImg.src ='/icon/exclamation-mark.png/';
    modalMessage.textContent = messageTitle;
    modalMessageBody.textContent = messageBody;

    modal.style.display = "block";

    modal.classList.remove('fade-out');
    modal.classList.add('fade-in');

    setTimeout(function() {
        modal.classList.remove('fade-in');
        modal.classList.add('fade-out');
    }, 3000);

    setTimeout(function() {
        modal.style.display = "none";
    }, 4000);
}

function fetchCategories() {
    fetch("/internal/category/list")
        .then(response => response.json())
        .then(categories => {
            populateCategoryDropdown(categories);
        })
        .catch(error => {
            console.error("Error fetching categories:", error);
    });
}

function fetchAttributes() {
    fetch("/internal/attribute/list")
        .then(response => response.json())
        .then(attributes => {
            populateAttributeDropdown(attributes);
        })
        .catch(error => {
            console.error("Error fetching attributes:", error);
    });
}
