window.onload = function() {
    clearAttributeModalInput();
    clearCategoryModalInput();
    fetchCategories();
    fetchAttributes();
//    openModal();
    showTab("Product");

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
document.addEventListener("click", function(event) {
      let modal = document.getElementById("myProductModal");
      if (event.target === modal) {
          event.stopPropagation(); // Prevent closing the modal
      }
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