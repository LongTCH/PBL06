let page = 0;
const size = 24;
let isLoading = false;

function loadMoreProducts() {
    if (isLoading) return;
    isLoading = true;
    page++;
    fetch(`/products?page=${page}&size=${size}`)
        .then(response => response.text())
        .then(html => {
            const tempDiv = document.createElement('div');
            tempDiv.innerHTML = html;
            const products = tempDiv.querySelectorAll('.product');
            products.forEach(product => {
                document.getElementById('productsContainer').appendChild(product);
            });
            if (products.length === 0) {
                window.removeEventListener('scroll', loadMoreProducts);
            }
            isLoading = false;
        })
        .catch(() => isLoading = false);
}

window.addEventListener('scroll', function () {
    if ((window.innerHeight + window.scrollY) >= document.body.offsetHeight - 500) {
        loadMoreProducts();
    }
});
// doi mau checkbox size
const checkboxes = document.querySelectorAll('.checkbox-container input');
checkboxes.forEach((checkbox) => {
    checkbox.addEventListener('change', function () {
        if (this.checked) {
            this.parentElement.style.backgroundColor = 'black';
            this.parentElement.style.color = 'white';
        } else {
            this.parentElement.style.backgroundColor = '';
            this.parentElement.style.color = '';
        }
    });
});

function loadProducts() {
    const formData = new FormData(document.getElementById('filterForm'));
    const queryString = new URLSearchParams(formData).toString();
    fetch(`/products/filter?${queryString}`)
        .then(response => response.text())
        .then(data => {
            document.getElementById('productsContainer').innerHTML = data;
        })
        .catch(error => console.error('Error:', error));
    return false; // Prevent form submission
}


document.addEventListener('DOMContentLoaded', function () {
    const priceSlider = document.getElementById('price-slider');
    noUiSlider.create(priceSlider, {
        start: [0, 3000000],
        connect: true,
        range: {
            'min': 0,
            'max': 3000000
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
        loadProducts();
    });
});


