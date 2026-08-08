<script lang="ts">
	import { CircleAlert, CircleCheck, CircleX, Info, X } from '@lucide/svelte';
	import type { Component } from 'svelte';
	import { fade } from 'svelte/transition';
	import Button from '$lib/basics/Button.svelte';
	import { toast, type ToastKind } from './toast.store.svelte';

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
	{#each toast.values as toastMessage (toastMessage.id)}
		{@const kind = types[toastMessage.kind]}
		<div class="alert {kind.class}" transition:fade={{ duration: 200 }}>
			<kind.icon />
			<span>{toastMessage.message}</span>
			<Button
				class="btn btn-xs {kind.btnClass}"
				icon={X}
				onclick={() => toast.dismiss(toastMessage.id)}
			/>
		</div>
	{/each}
</div>
