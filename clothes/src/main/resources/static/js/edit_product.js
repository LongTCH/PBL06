tinymce.init({
    selector: 'textarea',
    height: 300,
    plugins: 'image link media',
    toolbar: 'undo redo | formatselect | bold italic | alignleft aligncenter alignright | image link media',
    branding: false
});

let selectedFiles = new DataTransfer();

function previewImages(event) {
    const input = event.target;
    const imageGallery = document.querySelector('.image-gallery');
    Array.from(input.files).forEach(file => {
        selectedFiles.items.add(file);

        const reader = new FileReader();
        reader.onload = function (e) {
            const imgElement = document.createElement('img');
            imgElement.src = e.target.result;
            imgElement.classList.add('img-thumbnail');
            const imageContainer = document.createElement('div');
            imageContainer.classList.add('image-container');

            const deleteBtn = document.createElement('button');
            deleteBtn.innerText = 'X';
            deleteBtn.classList.add('delete-btn');
            deleteBtn.onclick = function () {
                const index = Array.from(imageGallery.children).indexOf(imageContainer);
                selectedFiles.items.remove(index);
                imageContainer.remove();
            };

            imageContainer.appendChild(imgElement);
            imageContainer.appendChild(deleteBtn);
            imageGallery.appendChild(imageContainer);
        };

        reader.readAsDataURL(file);
    });

    input.files = selectedFiles.files;
}

function deleteImage(button, imageUrl) {
    imageUrl = button.getAttribute('data-image-url');
    const imageContainer = button.parentElement;
    imageContainer.remove();
    const hiddenInput = document.getElementById('imagesToRemove');
    let imagesToRemove = hiddenInput.value.split(',').filter(url => url !== ""); // Remove empty strings
    if (!imagesToRemove.includes(imageUrl)) {
        imagesToRemove.push(imageUrl);
    }
    hiddenInput.value = imagesToRemove.join(',');
}

function deleteItem(element) {
    const row = element.closest('.row');
    if (row) {
        row.remove();
    }
}

function addColor() {
    const container = document.createElement('div');
    container.classList.add('color-item', 'row', 'align-items-center', 'mb-2');
    container.innerHTML = `
            <div class="col-10">
                <input type="text" class="form-control" name="colors[]"/>
            </div>
            <div class="col-2 text-end">
            <button type="button" class="btn btn-danger" onclick="deleteItem(this)">Xoá</button>
            </div>
        `;
    const addColorButton = document.getElementById('addColorButton'); // Ensure you have an ID for the button
    addColorButton.parentElement.insertBefore(container, addColorButton);
}

function addSize() {
    const container = document.createElement('div');
    container.classList.add('size-item', 'row', 'align-items-center', 'mb-2');
    container.innerHTML = `
            <div class="col-10">
            <input type="text" class="form-control" name="sizes[]">
            </div>
             <div class="col-2 text-end">
            <button type="button" class="btn btn-danger" onclick="deleteItem(this)">Xoá</button>
            </div>
        `;
    const addSizeButton = document.getElementById('addSizeButton');
    addSizeButton.parentElement.insertBefore(container, addSizeButton);
}