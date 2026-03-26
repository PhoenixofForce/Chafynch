<script lang="ts">
	import { resolve } from '$app/paths';
	import { page } from '$app/state';
	import { Clock, Compass, Server, Clipboard } from '@lucide/svelte';

	const status = page.status;
	const errorMessage = page.error?.error || page.error?.message;
	const backendMessage = page.error?.message;

	const timestamp = page.error?.timestamp ?? new Date().toISOString();
	const frontendPath = page.url.pathname;
	const backendPath = page.error?.path;
	const completeTrace = page.error?.trace;
	const trace = page.error?.trace?.split('\n').slice(0, 10);

	let tooltipText = $state('Copy');
	function onClick() {
		if (!completeTrace) return;
		tooltipText = 'Copied!';
		navigator.clipboard.writeText(completeTrace);
	}
</script>

<div class="flex h-full w-full flex-col p-16">
	<div class="prose min-w-full">
		<h1 class="mb-2">
			{status}
			{errorMessage}
		</h1>
		<div class="mt-0 w-full font-bold">
			<div class="badge badge-ghost"><Clock />{timestamp}</div>

			{#if backendPath}
				<div class="badge badge-ghost">
					<Server />{backendPath}
				</div>
			{/if}
			<div class="badge badge-ghost">
				<Compass />
				{frontendPath}
			</div>
		</div>

		{#if backendMessage}
			<h2 class="mb-2">{backendMessage}</h2>
		{/if}
		{#if trace}
			<div
				class="mockup-code relative mt-4 w-full overflow-x-scroll bg-warning text-warning-content"
			>
				{#each trace as line, i (i)}
					<pre
						data-prefix={i + 1}
						class="mt-0 mb-0 overflow-x-visible bg-warning py-0 text-warning-content"><code
							>{line}</code
						></pre>
				{/each}
				<div
					class="tooltip absolute tooltip-left top-3 right-6 tooltip-primary"
					data-tip={tooltipText}
				>
					<button class="btn btn-warning" onclick={onClick}>
						<Clipboard />
					</button>
				</div>
			</div>
		{/if}
		<h2 class="mb-1">This was not supposed to happen</h2>
		<p class="italic">
			You can go back to the <a href={resolve('/')}> home page </a>
		</p>
	</div>
</div>
