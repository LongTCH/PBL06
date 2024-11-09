document.addEventListener('DOMContentLoaded', function () {
    const fileInput = document.getElementById('fileInput');
    const imageContainer = document.getElementById('imageContainer');
    const image = document.getElementById('image');
    const cropButton = document.getElementById('cropButton');
    const croppedImageContainer = document.getElementById('croppedImageContainer');
    const croppedImage = document.getElementById('croppedImage');
    let cropper;

    fileInput.addEventListener('change', function (event) {
        const files = event.target.files;
        if (files && files.length > 0) {
            const file = files[0];
            const reader = new FileReader();
            reader.onload = function (e) {
                image.src = e.target.result;
                imageContainer.style.display = 'block';
                cropButton.style.display = 'block';
                if (cropper) {
                    cropper.destroy();
                }
                cropper = new Cropper(image, {
                    aspectRatio: 1,
                    viewMode: 1,
                });
            };
            reader.readAsDataURL(file);
        }
    });

    cropButton.addEventListener('click', function () {
        const canvas = cropper.getCroppedCanvas();
        croppedImage.src = canvas.toDataURL();
        croppedImageContainer.style.display = 'block';
        cropper.destroy();
        imageContainer.style.display = 'none';
        cropButton.style.display = 'none';

        const formData = new FormData();
        formData.append('image', canvas.toDataURL());
        fetch('/searchByImage', {
            method: 'POST',
            body: formData
        }).then(response => response.json())
            .then(data => {
                console.log(data);
            });
    });

});