<script lang="ts">
	import TeaForm from '../../TeaForm.svelte';

	let { data } = $props();
	let tea = $state(data.tea);
</script>

<TeaForm form={tea} />
