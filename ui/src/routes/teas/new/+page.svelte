<script lang="ts">
	import type { TeaDTO } from '$lib/api/types';
	import TeaForm from '../TeaForm.svelte';

	// Todo: dedup state within form
	let form = $state<TeaDTO>({
		name: '',
		cultivar: '',
		teaType: '',
		vendor: '',
		harvestYear: undefined,
		harvestLabel: undefined,
		descriptionMd: '',
		originCountry: '',
		originProvince: '',
		originCity: '',
		price: undefined,
		purchaseDate: '',
		weightGrams: undefined
	});
</script>

<TeaForm bind:form />
