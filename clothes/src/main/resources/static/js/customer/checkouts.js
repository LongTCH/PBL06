document.addEventListener('DOMContentLoaded', function () {
    // Fetch provinces
    fetch('https://esgoo.net/api-tinhthanh/1/0.htm')
        .then(response => response.json())
        .then(data_tinh => {
            if (data_tinh.error === 0) {
                const tinhSelect = document.getElementById('tinh');
                data_tinh.data.forEach(val_tinh => {
                    const option = document.createElement('option');
                    option.value = val_tinh.id;
                    option.textContent = val_tinh.full_name;
                    tinhSelect.appendChild(option);
                });

                tinhSelect.addEventListener('change', function () {
                    const idtinh = this.value;
                    fetch(`https://esgoo.net/api-tinhthanh/2/${idtinh}.htm`)
                        .then(response => response.json())
                        .then(data_quan => {
                            if (data_quan.error === 0) {
                                const quanSelect = document.getElementById('quan');
                                const phuongSelect = document.getElementById('phuong');
                                quanSelect.innerHTML = '<option value="0">Quận Huyện</option>';
                                phuongSelect.innerHTML = '<option value="0">Phường Xã</option>';
                                data_quan.data.forEach(val_quan => {
                                    const option = document.createElement('option');
                                    option.value = val_quan.id;
                                    option.textContent = val_quan.full_name;
                                    quanSelect.appendChild(option);
                                });

                                quanSelect.addEventListener('change', function () {
                                    const idquan = this.value;
                                    fetch(`https://esgoo.net/api-tinhthanh/3/${idquan}.htm`)
                                        .then(response => response.json())
                                        .then(data_phuong => {
                                            if (data_phuong.error === 0) {
                                                phuongSelect.innerHTML = '<option value="0">Phường Xã</option>';
                                                data_phuong.data.forEach(val_phuong => {
                                                    const option = document.createElement('option');
                                                    option.value = val_phuong.id;
                                                    option.textContent = val_phuong.full_name;
                                                    phuongSelect.appendChild(option);
                                                });
                                            }
                                        });
                                });
                            }
                        });
                });
            }
        });

    // Fetch cart data
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
    let cartContainer = document.getElementById('checkoutProductsContainer');
    let productHTML = '';

    products.forEach(product => {
        product.variants.forEach(variant => {
            productHTML += `
                <div class="product-card" data-product-id="${product.id}" data-variant-id="${variant.id}" data-price="${variant.price}">
                    <div class="product-image">
                        <img src="${product.images[0].url}" alt="product-image"/>
                        <span class="product-thumbnail-quantity">${variant.quantity}</span>
                    </div>
                    <div class="product-info">
                        <h5 class="product-name">${product.title}</h5>
                        <p>${variant.name}</p>  
                    </div>
                    <span class="quantity"></span>
                    <p class="product-total">${(variant.price * variant.quantity).toLocaleString('en-US')}đ</p>
                </div>
            `;
        });
    });

    cartContainer.innerHTML = productHTML;
    const totalPrice = calculateTotalPrice();
    document.getElementById('totalPrice').textContent = totalPrice.toLocaleString('en-US') + 'đ';
    console.log(totalPrice);
    if (totalPrice >= 699000) {
        document.getElementById('freeShipping').style.display = 'block';
    } else {
        document.getElementById('paidShipping').style.display = 'block';
    }
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
    return totalPrice;
}

document.getElementById('checkoutForm').addEventListener('submit', function (event) {
    event.preventDefault();

    const cart = JSON.parse(localStorage.getItem('cart')) || [];
    const items = cart.map(item => {
        const product = productData.find(p => p.id === item.id);
        const variant = product.variants.find(v => v.id === item.variantId);
        return {
            productId: item.id,
            variantId: item.variantId,
            quantity: item.quantity,
            price: variant.price
        };
    });

    const formData = {
        name: document.getElementById('fullName').value,
        email: document.getElementById('email').value,
        phone: document.getElementById('phone').value,
        address: {
            city: document.getElementById('tinh').options[document.getElementById('tinh').selectedIndex].text,
            district: document.getElementById('quan').options[document.getElementById('quan').selectedIndex].text,
            ward: document.getElementById('phuong').options[document.getElementById('phuong').selectedIndex].text,
            street: document.getElementById('address').value
        },
        items: items,
        totalPrice: calculateTotalPrice(),
        paymentMethod: document.querySelector('input[name="paymentMethod"]:checked').value
    };

    if (formData.paymentMethod === '2') { // Bank transfer
        fetch('/checkouts/submitOrder', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                amount: formData.totalPrice,
                orderInfo: 'Thanh toan don hang',
                data: formData
            })
        })
            .then(response => response.json())
            .then(data => {
                localStorage.removeItem('cart');
                window.location.href = data.redirectUrl;
            })
            .catch(error => console.error('Error:', error));
    } else {
        fetch('/checkouts/addOrder', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        })
            .then(response => response.json())
            .then(data => {
                if (data.status === "success") {
                    localStorage.removeItem('cart');
                    window.location.href = `/checkouts/success?orderId=${data.orderId}`;
                } else {
                    alert('Failed to place order. Please try again.');
                }
            })
            .catch(error => console.error('Error:', error));
    }
});