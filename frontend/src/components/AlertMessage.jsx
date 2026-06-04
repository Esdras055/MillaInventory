import { CheckCircle2, X, XCircle } from "lucide-react";
import { useEffect } from "react";

const alertStyles = {
  error: {
    container: "border-red-200 bg-red-50 text-red-700",
    icon: XCircle,
  },
  success: {
    container: "border-emerald-200 bg-emerald-50 text-emerald-700",
    icon: CheckCircle2,
  },
};

function AlertMessage({
  autoCloseMs,
  className = "mb-5",
  message,
  onClose,
  type = "success",
}) {
  const styles = alertStyles[type] || alertStyles.success;
  const Icon = styles.icon;

  useEffect(() => {
    if (!message || !autoCloseMs || !onClose) {
      return undefined;
    }

    const timeoutId = window.setTimeout(onClose, autoCloseMs);

    return () => {
      window.clearTimeout(timeoutId);
    };
  }, [autoCloseMs, message, onClose]);

  if (!message) {
    return null;
  }

  return (
    <div
      className={`flex items-start gap-3 rounded-lg border px-4 py-3 text-sm ${styles.container} ${className}`}
      role="alert"
    >
      <Icon className="mt-0.5 shrink-0" size={18} />
      <p className="flex-1">{message}</p>
      {onClose && (
        <button
          aria-label="Cerrar alerta"
          className="rounded-md p-1 hover:bg-white/60"
          onClick={onClose}
          type="button"
        >
          <X size={16} />
        </button>
      )}
    </div>
  );
}

export default AlertMessage;
