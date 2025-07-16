import cn from "@/libs/cn";
import type React from "react";

const PLACEMENT = {
  top: "modal-top",
  middle: "modal-middle",
  bottom: "modal-bottom",
  start: "modal-start",
  end: "modal-end",
};

export type ModalPlacement = keyof typeof PLACEMENT;

export type ModalProps = {
  children?: React.ReactNode;
  title?: React.ReactNode;
  actions?: React.ReactNode;
  visible?: boolean;
  onVisibleChange?: (visible: boolean) => void;
  placement?: ModalPlacement;
};

// modal-backdrop
// Part
// 	Label that covers the page when modal is open so we can close the modal by clicking outside
// modal-toggle
// Part
// 	Hidden checkbox that controls the state of modal
export default function Modal({
  children,
  title,
  actions,
  visible,
  onVisibleChange,
  placement = "middle",
}: ModalProps) {
  return (
    <dialog
      className={cn("modal", visible && "modal-open", PLACEMENT[placement])}
    >
      <div className="modal-box">
        {title && (
          <h3 className="font-bold text-lg flex justify-between items-start">
            {title}
            <span
              className="cursor-pointer hover:bg-indigo-50 rounded-full w-6 h-6 inline-flex items-center justify-center text-gray-600"
              onClick={() => onVisibleChange?.(false)}
            >
              &times;
            </span>
          </h3>
        )}
        {children}
        {actions && <div className="modal-action">{actions}</div>}
      </div>
      <div
        className="modal-backdrop"
        onClick={() => onVisibleChange?.(false)}
      />
    </dialog>
  );
}
