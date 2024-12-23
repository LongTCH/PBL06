let page = 0;
const size = 24;
let isLoading = false;

function loadMoreProducts() {
    if (isLoading) return;
    isLoading = true;
    page++;
    if(page <= size) filterProducts();
}

window.addEventListener('scroll', function () {
    if ((window.innerHeight + window.scrollY) >= document.body.scrollHeight - 500) {
        loadMoreProducts();
    }
});


function onChangeFilter() {
    document.getElementById('product-list').innerHTML = '';
    page = 0;
    filterProducts();
}

function filterProducts() {
    const formData = new FormData(document.getElementById('filterForm'));

    let minPrice = 0;
    let maxPrice = 3000000;
    const priceRange = document.getElementById('priceRange').value;

    switch (priceRange) {
        case '1':
            maxPrice = 1000000;
            break;
        case '2':
            minPrice = 1000000;
            maxPrice = 2000000;
            break;
        case '3':
            minPrice = 2000000;
            maxPrice = 4000000;
            break;
        case '4':
            minPrice = 4000000;
            maxPrice = 8000000;
            break;
        case '5':
            minPrice = 8000000;
            maxPrice = 16000000;
            break;
        case '6':
            minPrice = 16000000;
            maxPrice = 32000000;
            break;
        case '7':
            minPrice = 32000000;
            maxPrice = 40000000;
            break;
        default:
            minPrice = 0;
            break;
    }

    const path = window.location.pathname;
    const match = path.match(/\/group\/([^/]+)/);
    let groupId = null;

    if (match && match[1]) {
        groupId = match[1];
    } else {
        console.log('Group ID not found in pathname');
    }

    const filtersDto = {
        groups: [],
        minPrice: minPrice,
        maxPrice: maxPrice,
        size: size,
        page: page
    };

    formData.forEach((value, key) => {
        if (key.startsWith('groups[')) {
            const index = key.match(/\d+/)[0];
            const field = key.match(/\.(\w+)$/)[1];
            if (!filtersDto.groups[index]) {
                filtersDto.groups[index] = {id: '', name: '', selected: false};
            }
            if (field === 'selected') {
                filtersDto.groups[index][field] = value === 'true';
            } else {
                filtersDto.groups[index][field] = value;
            }
        }
    });

    if (groupId) {
        const group = filtersDto.groups.find(group => group.id === groupId);
        if (group) {
            group.selected = true;
        } else {
            console.log('Group not found in filtersDto');
        }
    }

    fetch('/products', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(filtersDto)
    })
    .then(response => response.text())
    .then(data => {
        document.getElementById('product-list').innerHTML += data;
        const tempDiv = document.createElement('div');
        tempDiv.innerHTML = data;
        const newTotal = tempDiv.querySelector('input#totalProductFilter').value;
        const totalProductElement = document.getElementById('totalProduct');
        totalProductElement.innerHTML = 'Sản phẩm: ' + newTotal;
        isLoading = false;
    })
    .catch(error => {
        console.error('Error:', error);
        isLoading = false;
    });

    return false;
}