from rembg import remove, new_session
from PIL import Image
import numpy as np
import torch
from torchvision import transforms

session_part = new_session("u2net_cloth_seg")


def remove_background(input_image, clothes_part):
    if (clothes_part == None):
        return input_image

    # Remove background
    output = remove(input_image, session=session_part)

    # Get the dimensions of the output image
    output_width, output_height = output.size

    # The output image height will be three times the original height
    original_width, original_height = input_image.size

    # Define the part height: one-third of the total output height
    part_height = output_height // 3

    if clothes_part == "upper":
        output_part = output.crop((0, 0, output_width, part_height))
    elif clothes_part == "lower":
        output_part = output.crop(
            (0, part_height, output_width, 2*part_height))
    else:
        upper_part = output.crop((0, 0, output_width, part_height))
        lower_part = output.crop(
            (0, part_height, output_width, 2*part_height))
        upper_part = np.array(upper_part.convert("RGB"))
        lower_part = np.array(lower_part.convert("RGB"))
        output_part = np.zeros(
            (original_height, original_width, 3), dtype=np.uint8)
        for i in range(original_width):
            for j in range(original_height):
                output_part[j][i] = upper_part[j][i] + lower_part[j][i]

        output_part = Image.fromarray(output_part)

    output_part = output_part.convert("RGB")

    return output_part


def resize_with_padding(img, target_size):
    """Resize the image while maintaining the aspect ratio and pad with zeros."""
    width, height = img.size
    aspect_ratio = width / height

    if aspect_ratio > 1:
        # Image is wider than tall
        new_width = target_size
        new_height = int(target_size / aspect_ratio)
    else:
        # Image is taller than wide
        new_height = target_size
        new_width = int(target_size * aspect_ratio)

    img = img.resize((new_width, new_height), Image.Resampling.LANCZOS)

    # Create a new image with padding (black padding by default)
    new_img = Image.new("RGB", (target_size, target_size),
                        (0, 0, 0))  # Black padding

    # Calculate padding
    padding_top = (target_size - new_height) // 2
    padding_left = (target_size - new_width) // 2

    # Paste the resized image into the center of the padding
    new_img.paste(img, (padding_left, padding_top))

    return new_img


def predict_class(img, model, class_names, clothes_part=None, top_k=3, device='cpu'):
    image_size = 300

    preprocess = transforms.Compose([
        transforms.Lambda(lambda img: remove_background(img, clothes_part)),
        transforms.Lambda(lambda img: resize_with_padding(
            img, image_size)),  # Resize and pad
        transforms.ToTensor(),  # Convert image to tensor
        transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[
                             0.229, 0.224, 0.225])  # Normalize image
    ])

    # Preprocess the image
    img_tensor = preprocess(img).unsqueeze(
        0)  # Add batch dimension (1, C, H, W)

    # Move model and tensor to the correct device (GPU or CPU)
    model = model.to(device)
    img_tensor = img_tensor.to(device)

    # Set the model to evaluation mode
    model.eval()

    # Perform the forward pass and get predictions
    with torch.no_grad():
        outputs = model(img_tensor)

        # Apply softmax to get probabilities
        probabilities = torch.nn.functional.softmax(outputs, dim=1)

        # Get top-k predictions and their indices
        top_probs, top_idx = torch.topk(probabilities, top_k)

    # Get the predicted class labels
    top_classes = [class_names[idx] for idx in top_idx[0]]
    top_probabilities = top_probs[0].tolist()

    # Combine the class labels and probabilities into a list of tuples
    top_k_results = list(zip(top_classes, top_probabilities))

    return top_k_results


if __name__ == "__main__":
    image = Image.open("test.jpg")
    output = remove_background(image, "full")
    output.save("output.jpg")
