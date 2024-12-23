let page = 0;
const size = 24;
let isLoading = false;

const saleId = document.getElementById('sale_id').textContent;
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


function filterProducts() {
    fetch(`/sales/${saleId}?page=${page}&size=${size}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.text())
    .then(data => {
        document.getElementById('product-list').innerHTML += data;
        const tempDiv = document.createElement('div');
        tempDiv.innerHTML = data;
//        const newTotal = tempDiv.querySelector('input#totalProductFilter').value;
//        const totalProductElement = document.getElementById('totalProduct');
//        totalProductElement.innerHTML = 'Sản phẩm: ' + newTotal;
        isLoading = false;
    })
    .catch(error => {
        console.error('Error:', error);
        isLoading = false;
    });

    return false;
}