document.addEventListener('DOMContentLoaded', function () {
    const predictUrl = document.getElementById('predictUrl').innerText;
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
//                    aspectRatio: 1,
                    viewMode: 1,
                });
            };
            reader.readAsDataURL(file);
        }
    });

    cropButton.addEventListener('click', function () {
        // Get the cropped canvas
            const canvas = cropper.getCroppedCanvas();

            // Convert the canvas to a Blob (binary data)
            canvas.toBlob(function(blob) {
                // Create a FormData object to send the file
                const formData = new FormData();
                formData.append('file', blob, 'cropped_image.png'); // 'file' is the key expected by FastAPI

                // Send the image to the FastAPI server
                fetch(predictUrl, {
                    method: 'POST',
                    body: formData
                }).then(response => response.json())
                .then(data => {
                    console.log(data);
                }).catch(error => {
                    console.error('Error:', error);
                });
            }, 'image/jpeg');
    });

});