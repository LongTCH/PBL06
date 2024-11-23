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

    // Create a FormData object from the form
    const formData = new FormData(document.getElementById('filterForm'));

    // Convert FormData to a FiltersDto object
    const filtersDto = {
        groups: [],
        minPrice: parseInt(formData.get('minPrice')) || 0,
        maxPrice: parseInt(formData.get('maxPrice')) || 3000000,
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
    // Send the form data to the server using fetch
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
        isLoading = false;});

    return false; // Prevent form submission
}

document.addEventListener('DOMContentLoaded', function () {
    const priceSlider = document.getElementById('price-slider');
    noUiSlider.create(priceSlider, {
        start: [0, 38000000],
        connect: true,
        range: {
            'min': 0,
            'max': 38000000
        },
        tooltips: [true, true],
        format: {
            to: function (value) {
                return Math.round(value);
            },
            from: function (value) {
                return Number(value);
            }
        }
    });

    const minPriceInput = document.getElementById('min-price');
    const maxPriceInput = document.getElementById('max-price');

    priceSlider.noUiSlider.on('update', function (values, handle) {
        if (handle === 0) {
            minPriceInput.value = values[handle];
        } else {
            maxPriceInput.value = values[handle];
        }
        onChangeFilter().setTimeOut(1500);
    });
});