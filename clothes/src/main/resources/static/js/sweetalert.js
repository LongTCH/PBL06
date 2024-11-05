document.addEventListener("DOMContentLoaded", function () {
    function confirmDelete(button) {
        const productId = button.getAttribute('data-id');
        Swal.fire({
            title: 'Bạn có chắc chắn xoá?',
            text: "Hành động này không thể hoàn tác!",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Xóa',
            cancelButtonText: 'Hủy'
        }).then((result) => {
            if (result.isConfirmed) {
                button.closest('form').submit();
            }
        });
    }

    window.confirmDelete = confirmDelete;
});
