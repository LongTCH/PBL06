document.addEventListener('DOMContentLoaded', function () {
    // Fetch provinces
    fetch('https://esgoo.net/api-tinhthanh/1/0.htm')
        .then(response => response.json())
        .then(data_tinh => {
            if (data_tinh.error === 0) {
                const tinhSelect = document.getElementById('tinh');
                data_tinh.data.forEach(val_tinh => {
                    const option = document.createElement('option');
                    option.value = val_tinh.full_name; // Hiển thị `name` trong value
                    option.textContent = val_tinh.full_name; // Hiển thị `name` trong dropdown
                    option.setAttribute('data-id', val_tinh.id); // Lưu ID vào data-id
                    tinhSelect.appendChild(option);
                });

                tinhSelect.addEventListener('change', function () {
                    const selectedOption = tinhSelect.options[tinhSelect.selectedIndex];
                    const idtinh = selectedOption.dataset.id; // Lấy ID từ data-id
                    console.log("ID tỉnh được chọn:", idtinh);

                    fetch(`https://esgoo.net/api-tinhthanh/2/${idtinh}.htm`)
                        .then(response => response.json())
                        .then(data_quan => {
                            if (data_quan.error === 0) {
                                const quanSelect = document.getElementById('quan');
                                const phuongSelect = document.getElementById('phuong');
                                quanSelect.innerHTML = '<option value="">Quận Huyện</option>';
                                phuongSelect.innerHTML = '<option value="">Phường Xã</option>';
                                data_quan.data.forEach(val_quan => {
                                    const option = document.createElement('option');
                                    option.value = val_quan.full_name; // Hiển thị `name` trong value
                                    option.textContent = val_quan.full_name;
                                    option.setAttribute('data-id', val_quan.id); // Lưu ID vào data-id
                                    quanSelect.appendChild(option);
                                });

                                quanSelect.addEventListener('change', function () {
                                    const selectedOption = quanSelect.options[quanSelect.selectedIndex];
                                    const idquan = selectedOption.dataset.id; // Lấy ID từ data-id
                                    console.log("ID quận được chọn:", idquan);

                                    fetch(`https://esgoo.net/api-tinhthanh/3/${idquan}.htm`)
                                        .then(response => response.json())
                                        .then(data_phuong => {
                                            if (data_phuong.error === 0) {
                                                phuongSelect.innerHTML = '<option value="">Phường Xã</option>';
                                                data_phuong.data.forEach(val_phuong => {
                                                    const option = document.createElement('option');
                                                    option.value = val_phuong.full_name; // Hiển thị `name` trong value
                                                    option.textContent = val_phuong.full_name;
                                                    option.setAttribute('data-id', val_phuong.id); // Lưu ID vào data-id
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
});
