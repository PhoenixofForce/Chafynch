<script lang="ts">
	import { toasts, dismiss, type ToastKind } from '$lib/data/toast.svelte';
	import { CircleAlert, CircleCheck, CircleX, Info, X } from '@lucide/svelte';
	import type { Component } from 'svelte';
	import { fade } from 'svelte/transition';
	import Button from './Button.svelte';

	const types: Record<ToastKind, { class: string; btnClass: string; icon: Component }> = {
		info: {
			class: 'alert-info',
			btnClass: 'btn-info',
			icon: Info
		},
		success: {
			class: 'alert-success',
			btnClass: 'btn-success',
			icon: CircleCheck
		},
		warning: {
			class: 'alert-warning',
			btnClass: 'btn-warning',
			icon: CircleAlert
		},
		error: {
			class: 'alert-error',
			btnClass: 'btn-error',
			icon: CircleX
		}
	};
</script>

<div class="toast toast-center toast-top z-50">
	{#each toasts as toast (toast.id)}
		{@const kind = types[toast.kind]}
		<div class="alert {kind.class}" transition:fade={{ duration: 200 }}>
			<kind.icon />
			<span>{toast.message}</span>
			<Button class="btn btn-xs {kind.btnClass}" onclick={() => dismiss(toast.id)} icon={X} />
		</div>
	{/each}
</div>
