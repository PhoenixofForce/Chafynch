<script lang="ts">
	let { children } = $props();
</script>

<div class="flex min-h-svh w-full flex-col items-center justify-center p-4">
	{@render children()}
</div>
