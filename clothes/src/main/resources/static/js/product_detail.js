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
