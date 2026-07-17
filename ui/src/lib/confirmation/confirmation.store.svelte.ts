import type { Component } from 'svelte';
import type { MouseEventHandler } from 'svelte/elements';

export type Confirmation = {
	title?: string;
	description?: string;
	cancel?: {
		label?: string;
		icon?: Component;
	};
	confirm?: {
		label?: string;
		icon?: Component;
		class?: string;
		onclick?: MouseEventHandler<HTMLButtonElement>;
	};
};

class ConfirmationState {
	state = $state<Confirmation | null>(null);

	show(c: Confirmation) {
		this.state = c;
	}
	hide() {
		this.state = null;
	}
}

export const confirmation = new ConfirmationState();
