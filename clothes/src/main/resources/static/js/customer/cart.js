function updateQuantity(button, change) {
    const productCard = button.closest('.product-card');
    const productId = productCard.getAttribute('data-product-id');
    const variantId = productCard.getAttribute('data-variant-id');
    const cart = JSON.parse(localStorage.getItem('cart')) || [];
    const cartItem = cart.find(item => item.id === productId && item.variantId === variantId);
    if (cartItem) {
        cartItem.quantity = Math.max(cartItem.quantity + change, 1);

        localStorage.setItem('cart', JSON.stringify(cart));

        const quantityContainer = productCard.querySelector('.quantity-container .quantity');
        quantityContainer.textContent = cartItem.quantity;

        const productTotal = productCard.querySelector('.product-total');
        const price = parseFloat(productCard.querySelector('.product-price').textContent.replace('đ', ''));
        productTotal.textContent = (price * cartItem.quantity).toLocaleString('en-US') + 'đ';

        const totalPrice = calculateTotalPrice();
        document.getElementById('totalPrice').textContent = totalPrice + 'đ';
    }
}

let productData = [];

document.addEventListener("DOMContentLoaded", function () {
    const cart = JSON.parse(localStorage.getItem('cart')) || [];

    if (cart.length > 0) {
        const params = new URLSearchParams();
        cart.forEach(item => {
            params.append('id', item.id);
        });

        fetch(`/cart/products?${params.toString()}`)
            .then(response => response.ok ? response.json() : Promise.reject('Network response was not ok'))
            .then(data => {
                productData = data;
                const filteredProducts = data.map(product => {
                    const matchingVariants = product.variants.filter(variant => {
                        const cartItem = cart.find(item => item.id === product.id && item.variantId === variant.id);
                        if (cartItem) {
                            variant.quantity = cartItem.quantity;
                            return true;
                        }
                        return false;
                    });
                    return {...product, variants: matchingVariants};
                }).filter(product => product.variants.length > 0);
                updateCartUI(filteredProducts);
            })
            .catch(error => console.error('Error:', error));
    }
});

function updateCartUI(products) {
    let cartContainer = document.getElementById('cartProductsContainer');
    let productHTML = '';

    products.forEach(product => {
        product.variants.forEach(variant => {
            productHTML += `
                <div class="product-card" data-product-id="${product.id}" data-variant-id="${variant.id}">
                    <div class="product-image">
                        <img src="${product.images[0].url}" alt="product-image"/>
                    </div>
                    <div class="product-info">
                        <h5 class="product-name">${product.title}</h5>
                        <p>${variant.name}</p>
                        <p class="product-price">${variant.price}đ</p>
                        
                    </div>
                    <p class="product-total">${variant.price * variant.quantity}đ</p>
                    <div class="product-action">
                        <button type="button" class="qty-btn decrease-quantity" onclick="updateQuantity(this, -1)">
                            <svg class="icon icon--minus" viewBox="0 0 10 2">
                                <path d="M10 0v2H0V0z"></path>
                            </svg>
                        </button>
                        <div class="quantity-container">
                            <span class="quantity">${variant.quantity}</span>
                        </div>
                        <button type="button" class="qty-btn increase-quantity" onclick="updateQuantity(this, 1)">
                            <svg class="icon icon--plus" viewBox="0 0 10 10">
                                <path d="M6 4h4v2H6v4H4V6H0V4h4V0h2v4z"></path>
                            </svg>
                        </button>
                        <button class="btn btn-danger m-lg-2" onclick="removeFromCart(this)">Xóa</button>
                    </div>
                </div>
            `;
        });
    });

    cartContainer.innerHTML = productHTML;

    const totalPrice = calculateTotalPrice();
    document.getElementById('totalPrice').textContent = totalPrice + 'đ';
}


function calculateTotalPrice() {
    const cart = JSON.parse(localStorage.getItem('cart')) || [];
    const totalPrice = cart.reduce((total, item) => {
        const product = productData.find(p => p.id === item.id);
        if (product) {
            const variant = product.variants.find(v => v.id === item.variantId);
            if (variant) {
                const price = parseFloat(variant.price);
                return total + (price * item.quantity);
            }
        }
        return total;
    }, 0);
    return totalPrice.toLocaleString('en-US') + 'đ';
}

function removeFromCart(button) {
    const productCard = button.closest('.product-card');
    const productId = productCard.getAttribute('data-product-id');
    const variantId = productCard.getAttribute('data-variant-id');
    let cart = JSON.parse(localStorage.getItem('cart')) || [];

    cart = cart.filter(item => !(item.id === productId && item.variantId === variantId));

    localStorage.setItem('cart', JSON.stringify(cart));

    productCard.remove();

    const totalPrice = calculateTotalPrice();
    document.getElementById('totalPrice').textContent = totalPrice;
}