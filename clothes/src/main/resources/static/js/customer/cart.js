function updateQuantity(button, change) {
    const productCard = button.closest('.product-card');
    const productId = productCard.getAttribute('data-product-id');
    const variantName = productCard.getAttribute('data-variant-name');
    const cart = JSON.parse(localStorage.getItem('cart')) || [];
    console.log('Cart:', productId, "cc", variantName);
    const cartItem = cart.find(item => item.id === productId && item.variantName === variantName);
    if (cartItem) {
        cartItem.quantity = Math.max(cartItem.quantity + change, 1);

        localStorage.setItem('cart', JSON.stringify(cart));

        const quantityContainer = productCard.querySelector('.quantity-container .quantity');
        quantityContainer.textContent = cartItem.quantity;

        const productTotal = productCard.querySelector('.product-total');
        const price = parseFloat(productCard.querySelector('.product-price').textContent.replace('đ', ''));
        productTotal.textContent = (price * cartItem.quantity) + 'đ';
    }
}

document.addEventListener("DOMContentLoaded", function () {
    const cart = JSON.parse(localStorage.getItem('cart')) || [];

    if (cart.length > 0) {
        const params = new URLSearchParams();
        cart.forEach(item => {
            params.append('ids', item.id);
            params.append('variantName', item.variantName);
        });

        fetch(`/cart/products?${params.toString()}`)
            .then(response => response.ok ? response.json() : Promise.reject('Network response was not ok'))
            .then(data => {
                const filteredProducts = data.map(product => {
                    const matchingVariants = product.variants.filter(variant => {
                        const cartItem = cart.find(item => item.id === product.id && item.variantName === variant.name);
                        if (cartItem) {
                            variant.quantity = cartItem.quantity;
                            variant.variantId = cartItem.variantId;
                            return true;
                        }
                        return false;
                    });
                    return { ...product, variants: matchingVariants };
                }).filter(product => product.variants.length > 0);
                console.log('Filtered products:', filteredProducts);
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
                <div class="product-card" data-product-id="${product.id}" data-variant-name="${variant.name}">
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
                        <button class="btn btn-danger m-lg-2">Xóa</button>
                    </div>
                </div>
            `;
        });
    });

    cartContainer.innerHTML = productHTML;
}



