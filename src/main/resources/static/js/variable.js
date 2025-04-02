let storeDebounceTimeout;
let storeCategoryDebounceTimeout;
let searchTimeout;
let categoryGlobalAction = 'Create';
let resultList = [];
let currentTab = "Product";
let variantId = '';
imageIds = []

let num = 0;
let currentImageIndex = 0;
let imageUUIDs = [];
let path = window.location.pathname;
let query = window.location.search;
let imageUrls = [];
let currentIndex = 0;
let variantAttributes = [];
let currentPage = 1;
let totalPage = 0;

let currentStoreProductPage = 1;
let totalStoreProductPage = 0;
let categoryGlobalId = 0;
let url = '';
let clearStoreFilter = false;
const itemsPerPage = 14;

let lastGroupNum = 0;
let groupNumReq = 0;
let selectedData = [];
let globalStockQuantities = 0;