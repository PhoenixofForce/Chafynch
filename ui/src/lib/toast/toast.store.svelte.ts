import { SvelteMap } from 'svelte/reactivity';

export type ToastKind = 'info' | 'success' | 'warning' | 'error';

export type Toast = {
	id: number;
	kind: ToastKind;
	message: string;
};

let nextId = 0;
const toasts = $state<Toast[]>([]);
const timers = new SvelteMap<number, ReturnType<typeof setTimeout>>();

function show(kind: ToastKind, message: string) {
	const id = nextId++;
	toasts.push({ id, kind, message });
	timers.set(
		id,
		setTimeout(() => dismiss(id), 4000)
	);
}

function infoToast(message: string) {
	show('info', message);
}

function successToast(message: string) {
	show('success', message);
}

function warningToast(message: string) {
	show('warning', message);
}

function errorToast(message: string) {
	show('error', message);
}

function dismiss(id: number) {
	const timer = timers.get(id);
	if (timer) {
		clearTimeout(timer);
		timers.delete(id);
	}
	const index = toasts.findIndex((e) => e.id === id);
	if (index >= 0) toasts.splice(index, 1);
}

export const toast = {
	values: toasts,
	info: infoToast,
	success: successToast,
	warning: warningToast,
	error: errorToast,
	dismiss
};
