// Todo: should come from the backend

export interface TastingNoteSubCategory {
	name: string;
	notes: string[];
}

export interface TastingNoteCategory {
	name: string;
	subCategories: TastingNoteSubCategory[];
}

export interface Infusion {
	startTime: Date;
	infusionTime?: number;
	rating?: number;
	tastingCategories: TastingNoteCategory[];
}

export interface Session {
	infusions: Infusion[];
}
