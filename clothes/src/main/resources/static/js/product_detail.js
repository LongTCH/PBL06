document.addEventListener("DOMContentLoaded", function () {
    const mainSwiper = new Swiper('.product-gallery__slide.swiper', {
        slidesPerView: 1,
        navigation: {
            nextEl: '.swiper-next',
            prevEl: '.swiper-prev',
        },
        thumbs: {
            swiper: {
                el: '.product-gallery__thumb.swiper',
                slidesPerView: 5,
                watchSlidesVisibility: true,
                watchSlidesProgress: true,
            },
        },
    });
    const thumbnails = document.querySelectorAll(".product-thumb");
    thumbnails.forEach((thumbnail, index) => {
        thumbnail.addEventListener("click", function () {
            mainSwiper.slideTo(index);
        });
    });
    const product_swiper = new Swiper('.list-product-slide.swiper', {
        slidesPerView: 5,
        navigation: {
            nextEl: '.swiper-next',
            prevEl: '.swiper-prev',
        },
    });
});

function selectRadio(element) {
    const parentSwatch = element.closest('.select-swap');
    const radios = parentSwatch.querySelectorAll('input[type="radio"]');
    radios.forEach(function (radio) {
        radio.checked = false;
        const swatchElement = radio.closest('.swatch-element');
        swatchElement.classList.remove('selected');
    });
    const input = element.querySelector('input[type="radio"]');
    input.checked = true;
    element.classList.add('selected');
}


function increaseQuantity() {
    let qtyInput = document.getElementById("quantity");
    let currentValue = parseInt(qtyInput.value);
    if (!isNaN(currentValue)) {
        qtyInput.value = currentValue + 1;
    }
}

function decreaseQuantity() {
    let qtyInput = document.getElementById("quantity");
    let currentValue = parseInt(qtyInput.value);
    if (!isNaN(currentValue) && currentValue > 1) {
        qtyInput.value = currentValue - 1;
    }
}


async function addToCart(redirectToCart = false) {
    const pathname = window.location.pathname;
    const parts = pathname.split('/');
    const productId = parts[parts.length - 1];
    const quantity = parseInt(document.getElementById('quantity').value, 10);
    const selectedColor = document.querySelector('input[name="option_1"]:checked')?.value;
    const selectedSize = document.querySelector('input[name="option_2"]:checked')?.value;
    if (!selectedColor || !selectedSize) {
        showToast('error', 'Vui lòng chọn màu sắc và kích thước');
        return;
    }

    const variantName = `${selectedColor} / ${selectedSize}`;
    const cart = JSON.parse(localStorage.getItem('cart') || '[]');

    try {
        const response = await fetch(`/cart/variants?id=${productId}&variantName=${encodeURIComponent(variantName)}`);
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const variant = await response.json();

        const existingProduct = cart.find(item => item.id === productId && item.variantId === variant.id);
        if (existingProduct) {
            existingProduct.quantity += quantity;
        } else {
            if (quantity > variant.quantity) {
                showToast('error', 'Số lượng sản phẩm không đủ');
                return;
            }
            cart.push({
                id: productId,
                variantId: variant.id,
                quantity: quantity,
            });
        }

        localStorage.setItem('cart', JSON.stringify(cart));
        showToast('success', 'Đã thêm sản phẩm vào giỏ hàng');

        // Cập nhật số lượng tổng sản phẩm trong giỏ
        const cartCount = document.getElementById('cartCount');
        const totalQuantity = cart.reduce((sum, item) => sum + item.quantity, 0);
        cartCount.innerText = totalQuantity;

        if (redirectToCart) {
            window.location.href = '/cart';
        }
    } catch (error) {
        console.error('Error:', error);
        showToast('error', 'Đã có lỗi xảy ra');
    }
}

const priceElement = document.getElementById('formatted-price');
const price = parseInt(priceElement.textContent.replace('đ', '').trim());
priceElement.textContent = price.toLocaleString('en-US') + 'đ';

const CompareAtprice = document.getElementById('formatted-CompareAtprice');
const CompareAtpriceValue = parseInt(CompareAtprice.textContent.replace('đ', '').trim());
CompareAtprice.textContent = CompareAtpriceValue.toLocaleString('en-US') + 'đ';