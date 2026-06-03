let stream = null;
let detectorTimer = null;
let zxingControls = null;
let lastDetected = '';
let lastDetectedAt = 0;

const supportedFormats = ['ean_13', 'ean_8', 'upc_a', 'upc_e', 'code_128'];

function shouldNotify(code) {
    const now = Date.now();
    if (code === lastDetected && now - lastDetectedAt < 1800) return false;
    lastDetected = code;
    lastDetectedAt = now;
    return true;
}

async function startWithBarcodeDetector(videoElement, onDetected) {
    stream = await navigator.mediaDevices.getUserMedia({ video: { facingMode: { ideal: 'environment' } }, audio: false });
    videoElement.srcObject = stream;
    await videoElement.play();

    const Detector = window.BarcodeDetector;
    const detector = new Detector({ formats: supportedFormats });
    detectorTimer = window.setInterval(async () => {
        if (!videoElement.videoWidth) return;
        try {
            const codes = await detector.detect(videoElement);
            const rawValue = codes?.[0]?.rawValue;
            if (rawValue && shouldNotify(rawValue)) onDetected(rawValue);
        } catch (error) {
            console.warn('No se pudo detectar el código con BarcodeDetector', error);
        }
    }, 350);
}

async function startWithZxing(videoElement, onDetected) {
    const ZXingBrowser = window.ZXingBrowser;
    if (!ZXingBrowser?.BrowserMultiFormatReader) {
        throw new Error('El lector ZXing no está disponible. Verifica tu conexión o captura el código manualmente.');
    }

    const reader = new ZXingBrowser.BrowserMultiFormatReader();
    zxingControls = await reader.decodeFromVideoDevice(undefined, videoElement, (result) => {
        const text = result?.getText?.();
        if (text && shouldNotify(text)) onDetected(text);
    });
}

export async function startScanner(videoElement, onDetected) {
    if (!navigator.mediaDevices?.getUserMedia) {
        throw new Error('Este navegador no permite usar cámara desde esta página. Usa localhost/HTTPS o captura el código manualmente.');
    }

    stopScanner();
    lastDetected = '';
    lastDetectedAt = 0;

    if ('BarcodeDetector' in window) {
        try {
            await startWithBarcodeDetector(videoElement, onDetected);
            return 'BarcodeDetector';
        } catch (error) {
            stopScanner();
            console.warn('BarcodeDetector no pudo iniciar, se intenta ZXing', error);
        }
    }

    await startWithZxing(videoElement, onDetected);
    return 'ZXing';
}

export function stopScanner() {
    if (detectorTimer) {
        window.clearInterval(detectorTimer);
        detectorTimer = null;
    }

    if (zxingControls?.stop) zxingControls.stop();
    zxingControls = null;

    if (stream) {
        stream.getTracks().forEach((track) => track.stop());
        stream = null;
    }
}
