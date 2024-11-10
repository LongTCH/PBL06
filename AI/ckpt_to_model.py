import torch
from torchvision.models import efficientnet_b3
from torch import nn


def get_model(model_name, num_classes, parallel=False):
    model = efficientnet_b3()
    num_ftrs = model.classifier[1].in_features
    model.classifier[1] = nn.Linear(num_ftrs, num_classes)
    return model


def get_image_size_for_model(model_name):
    if model_name == "efficientnet_b0":
        return 224

    elif model_name == "efficientnet_b3":
        return 300

    elif model_name == "efficientnet_b4":
        return 380

    else:
        raise ValueError(f"Unknown model name: {model_name}")


class_names = ["ao_2_day", "ao_ba_lo", "ao_da", "ao_dai", "ao_khoac_the_thao",
               "ao_len", "ao_mangto", "ao_so_mi", "ao_thun", "ao_tre_vai",
               "ao_vest", "chan_vay_dai", "chan_vay_ngan", "dam_maxi",
               "dam_om", "do_ngu_do_mac_nha", "quan_dai", "quan_jean",
               "quan_short"]
device = "cpu"

model = get_model("efficientnet_b3", num_classes=len(
    class_names), parallel=False)
model.load_state_dict(torch.load(
    'clothes_efficientnet_b3_21.pth', weights_only=False, map_location=torch.device(device)))
model.to(device)

torch.save(model, 'model.pth')
