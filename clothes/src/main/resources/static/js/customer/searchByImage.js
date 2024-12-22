let page = 0;
const size = 24;
let isLoading = false;
let predictions = [];
function loadMoreProducts() {
    if (predictions.length === 0) return;
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

function filterProducts(){
    if (predictions.length === 0) return;
    fetch('/products/search-image', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            predictions: predictions,
            page: page,
            size: size
        })
    }).then(response => response.text())
    .then(data => {
        document.getElementById('product-list').innerHTML += data;
                const tempDiv = document.createElement('div');
                tempDiv.innerHTML = data;
                isLoading = false;
        }).catch(error => {
            console.error('Error:', error);
            isLoading = false;
        });
}
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
                predictions = []
                page = 0;
                document.getElementById('product-list').innerHTML = '';
                // Send the image to the FastAPI server
                fetch(predictUrl, {
                    method: 'POST',
                    body: formData
                }).then(response => response.json())
                .then(data => {
                    if (data.prediction[0][1] > 0.8)
                    {
                        predictions.push(data.prediction[0][0]);
                    } else
                    {
                        for (let i = 0; i < data.prediction.length; i++) {
                            predictions.push(data.prediction[i][0]);
                        }
                    }
                    loadMoreProducts();
                }).catch(error => {
                    console.error('Error:', error);
                });
            }, 'image/jpeg');
    });
});