from fastapi.middleware.cors import CORSMiddleware
from fastapi import FastAPI
import uvicorn
from fastapi import File, UploadFile
from fastapi.responses import JSONResponse
from PIL import Image
import io
from PIL import Image
import torch
from utils import predict_class

class_names = ["ao_2_day", "ao_ba_lo", "ao_da", "ao_dai", "ao_khoac_the_thao",
               "ao_len", "ao_mangto", "ao_so_mi", "ao_thun", "ao_tre_vai",
               "ao_vest", "chan_vay_dai", "chan_vay_ngan", "dam_maxi",
               "dam_om", "do_ngu_do_mac_nha", "quan_dai", "quan_jean",
               "quan_short"]

device = "cuda" if torch.cuda.is_available() else "cpu"

model = torch.load('model.pth', map_location=torch.device(
    device), weights_only=False)


app = FastAPI()

origins = ["*"]

app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


@app.post("/predict")
async def predict(file: UploadFile = File(...), clothes_part: str = None):
    contents = await file.read()
    img = Image.open(io.BytesIO(contents))
    top_k_results = predict_class(
        img, model, class_names, clothes_part, top_k=2, device=device)

    return JSONResponse(content={"prediction": top_k_results})


@app.get("/")
async def root():
    return {"message": "Hello World"}

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)

