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
