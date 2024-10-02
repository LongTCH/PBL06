function showToast(type, message) {
    console.log(type + ": " + message);
    const toast = document.createElement('div');
    toast.className = `toast ${type}`;

    const icon = document.createElement('img');
    icon.className = 'toast-icon';
    if (type === 'error') {
        icon.src = '/images/error.png';
    } else if (type === 'success') {
        icon.src = '/images/success.png'
    }

    const toastContent = document.createElement('span');
    toastContent.innerText = message;

    const closeButton = document.createElement('button');
    closeButton.className = 'close-button';
    closeButton.innerHTML = '&times;'; // Ký tự '×' để hiển thị dấu nhân
    closeButton.onclick = () => {
        toast.classList.remove('show');
        setTimeout(() => {
            if (toast.parentElement) {
                toast.parentElement.removeChild(toast);
            }
            // Xóa container nếu không còn toast nào
            if (toastContainer && toastContainer.children.length === 0) {
                document.body.removeChild(toastContainer);
            }
        }, 300);
    };

    const progressBar = document.createElement('div');
    progressBar.className = 'progress-bar';

    toast.appendChild(icon);
    toast.appendChild(toastContent);
    toast.appendChild(closeButton);
    toast.appendChild(progressBar);

    let toastContainer = document.querySelector('.toast-container');
    if (!toastContainer) {
        toastContainer = document.createElement('div');
        toastContainer.className = 'toast-container';
        document.body.appendChild(toastContainer);
    }

    toastContainer.appendChild(toast);

    setTimeout(() => {
        toast.classList.add('show');
    }, 100);
    let progressInterval;
    let width = 100;
    const duration = 10000;
    const stepTime = duration / 100;
    progressInterval = setInterval(() => {
        width--;
        progressBar.style.width = width + '%';
        if (width <= 0) {
            clearInterval(progressInterval);
        }
    }, stepTime);

    setTimeout(() => {
        toast.classList.remove('show');
        setTimeout(() => {
            if (toast.parentElement) {
                toast.parentElement.removeChild(toast);
            }
            if (toastContainer && toastContainer.children.length === 0) {
                document.body.removeChild(toastContainer);
            }
        }, 300);
    }, duration);
}
